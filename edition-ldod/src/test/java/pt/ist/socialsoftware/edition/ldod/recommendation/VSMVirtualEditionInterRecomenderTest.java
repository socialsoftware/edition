package pt.ist.socialsoftware.edition.ldod.recommendation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.queryparser.classic.ParseException;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.NullHeteronym;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.TaxonomyProperty;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.TextProperty;
import pt.ist.socialsoftware.edition.ldod.search.Indexer;
import pt.ist.socialsoftware.edition.ldod.topicmodeling.TopicModeler;
import pt.ist.socialsoftware.edition.ldod.utils.TopicListDTO;

public class VSMVirtualEditionInterRecomenderTest extends TestWithFragmentsLoading {
	private static final String ACRONYM = "TestRecommendations";

	private static Logger logger = LoggerFactory.getLogger(VSMVirtualEditionInterRecomenderTest.class);

	private static VSMRecommender<VirtualEditionInter> recommender;

	private VirtualEdition virtualEdition;

	@Override
	protected String[] fragmentsToLoad4Test() {
		String[] fragments = { "001.xml", "181.xml", "593.xml" };

		return fragments;
	}

	// Assuming that the 4 expert editions and user ars are in the database
	@Override
	protected void populate4Test() {

		LdoD ldoD = LdoD.getInstance();
		ExpertEdition pizarroEdition = (ExpertEdition) ldoD.getEdition(Edition.PIZARRO_EDITION_ACRONYM);

		LdoDUser userArs = ldoD.getUser("ars");
		// create virtual edition
		this.virtualEdition = ldoD.createVirtualEdition(userArs, ACRONYM, "Name", LocalDate.now(), true,
				pizarroEdition);

		// create taxonomy
		TopicModeler modeler = new TopicModeler();
		TopicListDTO topicListDTO = null;
		try {
			topicListDTO = modeler.generate(userArs, this.virtualEdition, 50, 6, 11, 10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.virtualEdition.getTaxonomy().createGeneratedCategories(topicListDTO);

		// create recommender
		recommender = new VSMVirtualEditionInterRecommender();
	}

	@Override
	protected void unpopulate4Test() {
		TestLoadUtils.cleanDatabaseButCorpus();
	}

	@Test
	@Atomic
	public void testGetMostSimilarItemForHeteronym() {
		VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(ACRONYM);
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			if (inter.getLastUsed().getHeteronym() != null) {
				virtualEditionInter = (VirtualEditionInter) inter;
				break;
			}
		}

		List<Property> properties = new ArrayList<>();
		properties.add(new HeteronymProperty(1.0));

		VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter, new HashSet<>(virtualEdition
				.getIntersSet().stream().map(VirtualEditionInter.class::cast).collect(Collectors.toSet())), properties);

		assertTrue(virtualEditionInter != result);
		assertEquals(virtualEditionInter.getLastUsed().getHeteronym(), result.getLastUsed().getHeteronym());
	}

	@Test
	@Atomic
	public void testGetMostSimilarItemForNoHeteronym() {
		VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(ACRONYM);
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			if (inter.getLastUsed().getHeteronym() == NullHeteronym.getNullHeteronym()) {
				virtualEditionInter = (VirtualEditionInter) inter;
				break;
			}
		}

		List<Property> properties = new ArrayList<>();
		properties.add(new HeteronymProperty(1.0));

		VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter, new HashSet<>(virtualEdition
				.getIntersSet().stream().map(VirtualEditionInter.class::cast).collect(Collectors.toSet())), properties);

		assertTrue(virtualEditionInter != result);
		assertEquals(NullHeteronym.getNullHeteronym(), result.getLastUsed().getHeteronym());
	}

	@Test
	@Atomic
	public void testGetMostSimilarItemForDate() {
		VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(ACRONYM);
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			if (inter.getLastUsed().getLdoDDate() != null
					&& inter.getLastUsed().getLdoDDate().getDate().getYear() == 1929) {
				virtualEditionInter = (VirtualEditionInter) inter;
				break;
			}
		}

		List<Property> properties = new ArrayList<>();
		properties.add(new DateProperty(1.0));

		VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter, new HashSet<>(virtualEdition
				.getIntersSet().stream().map(VirtualEditionInter.class::cast).collect(Collectors.toSet())), properties);

		assertTrue(virtualEditionInter != result);
		assertEquals(virtualEditionInter.getLastUsed().getLdoDDate().getDate().getYear(),
				result.getLastUsed().getLdoDDate().getDate().getYear());
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void testGetMostSimilarItemForNoDate() {
		VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(ACRONYM);
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			if (inter.getLastUsed().getLdoDDate() == null) {
				virtualEditionInter = (VirtualEditionInter) inter;
				break;
			}
		}

		List<Property> properties = new ArrayList<>();
		properties.add(new DateProperty(1.0));

		VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter, new HashSet<>(virtualEdition
				.getIntersSet().stream().map(VirtualEditionInter.class::cast).collect(Collectors.toSet())), properties);

		assertTrue(virtualEditionInter != result);
		assertNotNull(result.getLastUsed().getLdoDDate());
		assertNull(virtualEditionInter.getLastUsed().getLdoDDate());
	}

	@Test
	@Atomic
	public void testGetMostSimilarItemForTaxonomy() {
		VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(ACRONYM);
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			if (!((VirtualEditionInter) inter).getTagSet().isEmpty()) {
				virtualEditionInter = (VirtualEditionInter) inter;
				break;
			}
		}

		List<Property> properties = new ArrayList<>();
		properties.add(new TaxonomyProperty(1.0, virtualEdition.getTaxonomy(), Property.PropertyCache.OFF));

		VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter, new HashSet<>(virtualEdition
				.getIntersSet().stream().map(VirtualEditionInter.class::cast).collect(Collectors.toSet())), properties);

		assertTrue(virtualEditionInter != result);
		assertFalse(virtualEditionInter.getTagSet().stream().map(t -> t.getCategory())
				.anyMatch(result.getTagSet().stream().map(t -> t.getCategory()).collect(Collectors.toSet())::contains));
	}

	@Test
	@Atomic
	public void testGetMostSimilarItemForText() throws IOException, ParseException {
		VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(ACRONYM);
		VirtualEditionInter virtualEditionInter = null;
		Indexer indexer = Indexer.getIndexer();

		for (FragInter inter : virtualEdition.getIntersSet()) {
			if (indexer.getTFIDFTerms(inter.getFragment(), TextProperty.NUMBER_OF_TERMS).contains("cadeira")) {
				virtualEditionInter = (VirtualEditionInter) inter;
				break;
			}
		}

		List<Property> properties = new ArrayList<>();
		properties.add(new TextProperty(1.0));

		VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter, new HashSet<>(virtualEdition
				.getIntersSet().stream().map(VirtualEditionInter.class::cast).collect(Collectors.toSet())), properties);

		assertTrue(virtualEditionInter != result);
//		assertTrue(indexer.getTFIDFTerms(virtualEditionInter.getFragment(), TextProperty.NUMBER_OF_TERMS).stream()
//				.anyMatch(indexer.getTFIDFTerms(result.getFragment(), TextProperty.NUMBER_OF_TERMS)::contains));
	}

	@Test
	@Atomic
	public void testGetMostSimilarItemForAll() throws IOException, ParseException {
		VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(ACRONYM);
		VirtualEditionInter virtualEditionInter = null;
		Indexer indexer = Indexer.getIndexer();

		for (FragInter inter : virtualEdition.getIntersSet()) {
			if (indexer.getTFIDFTerms(inter.getFragment(), TextProperty.NUMBER_OF_TERMS).contains("cadeira")) {
				virtualEditionInter = (VirtualEditionInter) inter;
				break;
			}
		}

		List<Property> properties = new ArrayList<>();
		properties.add(new HeteronymProperty(1.0));
		properties.add(new DateProperty(1.0));
		properties.add(new TaxonomyProperty(1.0, virtualEdition.getTaxonomy(), Property.PropertyCache.OFF));
		properties.add(new TextProperty(1.0));

		VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter, new HashSet<>(virtualEdition
				.getIntersSet().stream().map(VirtualEditionInter.class::cast).collect(Collectors.toSet())), properties);

		assertTrue(virtualEditionInter != result);
		assertEquals(virtualEditionInter.getLastUsed().getHeteronym(), result.getLastUsed().getHeteronym());
		assertEquals(virtualEditionInter.getLastUsed().getLdoDDate().getDate().getYear(),
				result.getLastUsed().getLdoDDate().getDate().getYear());
		assertFalse(virtualEditionInter.getTagSet().stream().map(t -> t.getCategory())
				.anyMatch(result.getTagSet().stream().map(t -> t.getCategory()).collect(Collectors.toSet())::contains));
//		assertFalse(indexer.getTFIDFTerms(virtualEditionInter.getFragment(), TextProperty.NUMBER_OF_TERMS).stream()
//				.anyMatch(indexer.getTFIDFTerms(result.getFragment(), TextProperty.NUMBER_OF_TERMS)::contains));
	}

}
