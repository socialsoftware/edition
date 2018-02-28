package pt.ist.socialsoftware.edition.core.recommendation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.joda.time.LocalDate;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.core.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.core.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.core.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.core.search.Indexer;
import pt.ist.socialsoftware.edition.core.topicmodeling.TopicModeler;
import pt.ist.socialsoftware.edition.core.utils.TopicListDTO;
import pt.ist.socialsoftware.edition.core.domain.Edition;
import pt.ist.socialsoftware.edition.core.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.core.domain.FragInter;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.LdoDUser;
import pt.ist.socialsoftware.edition.core.domain.NullHeteronym;
import pt.ist.socialsoftware.edition.core.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.core.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.core.recommendation.properties.TaxonomyProperty;
import pt.ist.socialsoftware.edition.core.recommendation.properties.TextProperty;

public class VSMVirtualEditionInterRecomenderTest {
	private static Logger logger = LoggerFactory.getLogger(VSMVirtualEditionInterRecomenderTest.class);

	private static VirtualEdition virtualEdition = null;
	private static Set<VirtualEditionInter> virtualEditionInters = null;
	private static VSMRecommender<VirtualEditionInter> recommender;

	// Assuming that the 4 expert editions and user ars are in the database
	@BeforeClass
	public static void setUp() throws IOException, WriteOnReadError, NotSupportedException, SystemException {
		FenixFramework.getTransactionManager().begin(false);

		LdoD ldoD = LdoD.getInstance();
		ExpertEdition pizarroEdition = (ExpertEdition) ldoD.getEdition(Edition.PIZARRO_EDITION_ACRONYM);

		LdoDUser userArs = ldoD.getUser("ars");
		// create virtual edition
		virtualEdition = ldoD.createVirtualEdition(userArs, "$Test$Recommendations$", "$Test$Recommendations$",
				LocalDate.now(), true, pizarroEdition);
		virtualEditionInters = virtualEdition.getIntersSet().stream().map(VirtualEditionInter.class::cast)
				.collect(Collectors.toSet());

		// create taxonomy
		TopicModeler modeler = new TopicModeler();
		TopicListDTO topicListDTO = modeler.generate(userArs, virtualEdition, 50, 6, 11, 100);
		virtualEdition.getTaxonomy().createGeneratedCategories(topicListDTO);

		// create recommender
		recommender = new VSMVirtualEditionInterRecommender();
	}

	@AfterClass
	public static void tearDown() throws IllegalStateException, SecurityException, SystemException {
		// remove virtual edition
		FenixFramework.getTransactionManager().rollback();
	}

	@Test
	public void testGetMostSimilarItemForHeteronym() {
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			if (inter.getLastUsed().getHeteronym() != null) {
				virtualEditionInter = (VirtualEditionInter) inter;
				break;
			}
		}

		List<Property> properties = new ArrayList<>();
		properties.add(new HeteronymProperty(1.0));

		VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter,
				new HashSet<>(virtualEditionInters), properties);

		assertTrue(virtualEditionInter != result);
		assertEquals(virtualEditionInter.getLastUsed().getHeteronym(), result.getLastUsed().getHeteronym());
	}

	@Test
	public void testGetMostSimilarItemForNoHeteronym() {
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			if (inter.getLastUsed().getHeteronym() == NullHeteronym.getNullHeteronym()) {
				virtualEditionInter = (VirtualEditionInter) inter;
				break;
			}
		}

		List<Property> properties = new ArrayList<>();
		properties.add(new HeteronymProperty(1.0));

		VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter,
				new HashSet<>(virtualEditionInters), properties);

		assertTrue(virtualEditionInter != result);
		assertEquals(NullHeteronym.getNullHeteronym(), result.getLastUsed().getHeteronym());
	}

	@Test
	public void testGetMostSimilarItemForDate() {
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			if (inter.getLastUsed().getLdoDDate() != null
					&& inter.getLastUsed().getLdoDDate().getDate().getYear() == 1914) {
				virtualEditionInter = (VirtualEditionInter) inter;
				break;
			}
		}

		List<Property> properties = new ArrayList<>();
		properties.add(new DateProperty(1.0));

		VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter,
				new HashSet<>(virtualEditionInters), properties);

		assertTrue(virtualEditionInter != result);
		Assert.assertEquals(virtualEditionInter.getLastUsed().getLdoDDate().getDate().getYear(),
				result.getLastUsed().getLdoDDate().getDate().getYear());
	}

	@Test
	public void testGetMostSimilarItemForNoDate() {
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			if (inter.getLastUsed().getLdoDDate() == null) {
				virtualEditionInter = (VirtualEditionInter) inter;
				break;
			}
		}

		List<Property> properties = new ArrayList<>();
		properties.add(new DateProperty(1.0));

		VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter,
				new HashSet<>(virtualEditionInters), properties);

		assertTrue(virtualEditionInter != result);
		assertNull(result.getLastUsed().getLdoDDate());
	}

	@Test
	public void testGetMostSimilarItemForTaxonomy() {
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			if (!((VirtualEditionInter) inter).getTagSet().isEmpty()) {
				virtualEditionInter = (VirtualEditionInter) inter;
				break;
			}
		}

		List<Property> properties = new ArrayList<>();
		properties.add(new TaxonomyProperty(1.0, virtualEdition.getTaxonomy(), Property.PropertyCache.OFF));

		VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter,
				new HashSet<>(virtualEditionInters), properties);

		assertTrue(virtualEditionInter != result);
		assertTrue(virtualEditionInter.getTagSet().stream().map(t -> t.getCategory())
				.anyMatch(result.getTagSet().stream().map(t -> t.getCategory()).collect(Collectors.toSet())::contains));
	}

	@Test
	public void testGetMostSimilarItemForText() throws IOException, ParseException {
		VirtualEditionInter virtualEditionInter = null;
		Indexer indexer = Indexer.getIndexer();

		for (FragInter inter : virtualEdition.getIntersSet()) {
			if (indexer.getTFIDFTerms(inter.getFragment(), TextProperty.NUMBER_OF_TERMS).contains("attitude")) {
				virtualEditionInter = (VirtualEditionInter) inter;
				break;
			}
		}

		List<Property> properties = new ArrayList<>();
		properties.add(new TextProperty(1.0));

		VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter,
				new HashSet<>(virtualEditionInters), properties);

		assertTrue(virtualEditionInter != result);
		assertTrue(indexer.getTFIDFTerms(virtualEditionInter.getFragment(), TextProperty.NUMBER_OF_TERMS).stream()
				.anyMatch(indexer.getTFIDFTerms(result.getFragment(), TextProperty.NUMBER_OF_TERMS)::contains));
	}

	@Test
	public void testGetMostSimilarItemForAll() throws IOException, ParseException {
		VirtualEditionInter virtualEditionInter = null;
		Indexer indexer = Indexer.getIndexer();

		for (FragInter inter : virtualEdition.getIntersSet()) {
			if (indexer.getTFIDFTerms(inter.getFragment(), TextProperty.NUMBER_OF_TERMS).contains("antonio")) {
				virtualEditionInter = (VirtualEditionInter) inter;
				break;
			}
		}

		List<Property> properties = new ArrayList<>();
		properties.add(new HeteronymProperty(1.0));
		properties.add(new DateProperty(1.0));
		properties.add(new TaxonomyProperty(1.0, virtualEdition.getTaxonomy(), Property.PropertyCache.OFF));
		properties.add(new TextProperty(1.0));

		VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter,
				new HashSet<>(virtualEditionInters), properties);

		assertTrue(virtualEditionInter != result);
		assertEquals(virtualEditionInter.getLastUsed().getHeteronym(), result.getLastUsed().getHeteronym());
		Assert.assertEquals(virtualEditionInter.getLastUsed().getLdoDDate().getDate().getYear(),
				result.getLastUsed().getLdoDDate().getDate().getYear());
		assertTrue(virtualEditionInter.getTagSet().stream().map(t -> t.getCategory())
				.anyMatch(result.getTagSet().stream().map(t -> t.getCategory()).collect(Collectors.toSet())::contains));
		assertTrue(indexer.getTFIDFTerms(virtualEditionInter.getFragment(), TextProperty.NUMBER_OF_TERMS).stream()
				.anyMatch(indexer.getTFIDFTerms(result.getFragment(), TextProperty.NUMBER_OF_TERMS)::contains));
	}

}
