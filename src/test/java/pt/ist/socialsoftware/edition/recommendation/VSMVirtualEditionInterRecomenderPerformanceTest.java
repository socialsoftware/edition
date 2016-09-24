package pt.ist.socialsoftware.edition.recommendation;

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
import org.junit.BeforeClass;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.mallet.TopicModeler;
import pt.ist.socialsoftware.edition.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.TaxonomyProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TextProperty;
import pt.ist.socialsoftware.edition.utils.TopicListDTO;

public class VSMVirtualEditionInterRecomenderPerformanceTest {
	private static VirtualEdition virtualEdition = null;
	private static Set<VirtualEditionInter> virtualEditionInters = null;
	private static VSMRecommender<VirtualEditionInter> recommender;

	// Assuming that the 4 expert editions and user ars are in the database
	@BeforeClass
	public static void setUp() throws IOException, WriteOnReadError, NotSupportedException, SystemException {
		FenixFramework.getTransactionManager().begin(false);

		LdoD ldoD = LdoD.getInstance();
		ExpertEdition pizarroEdition = (ExpertEdition) ldoD.getEdition(ExpertEdition.PIZARRO_ACRONYM);

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
	public void testGetMostSimilarItemForAllAsList() throws IOException, ParseException {
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			virtualEditionInter = (VirtualEditionInter) inter;
			break;
		}

		List<Property> properties = new ArrayList<Property>();
		properties.add(new HeteronymProperty(1.0));
		properties.add(new DateProperty(1.0));
		properties.add(new TaxonomyProperty(1.0, virtualEdition.getTaxonomy()));
		properties.add(new TextProperty(1.0));

		List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
				new HashSet<VirtualEditionInter>(virtualEditionInters), properties);

		assertTrue(result.size() != 0);
	}

	@Test
	public void testGetMostSimilarItemForAllAsListTwo() throws IOException, ParseException {
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			virtualEditionInter = (VirtualEditionInter) inter;
			break;
		}

		List<Property> properties = new ArrayList<Property>();
		properties.add(new HeteronymProperty(1.0));
		properties.add(new DateProperty(1.0));
		properties.add(new TaxonomyProperty(1.0, virtualEdition.getTaxonomy()));
		properties.add(new TextProperty(1.0));

		List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
				new HashSet<VirtualEditionInter>(virtualEditionInters), properties);

		assertTrue(result.size() != 0);
	}

	@Test
	public void testGetMostSimilarItemForDateAsList() throws IOException, ParseException {
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			virtualEditionInter = (VirtualEditionInter) inter;
			break;
		}

		List<Property> properties = new ArrayList<Property>();
		properties.add(new DateProperty(1.0));

		List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
				new HashSet<VirtualEditionInter>(virtualEditionInters), properties);

		assertTrue(result.size() != 0);
	}

	@Test
	public void testGetMostSimilarItemForTaxonomyAsList() throws IOException, ParseException {
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			virtualEditionInter = (VirtualEditionInter) inter;
			break;
		}

		List<Property> properties = new ArrayList<Property>();
		properties.add(new TaxonomyProperty(1.0, virtualEdition.getTaxonomy()));

		List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
				new HashSet<VirtualEditionInter>(virtualEditionInters), properties);

		assertTrue(result.size() != 0);
	}

	@Test
	public void testGetMostSimilarItemForTextAsListOneAgain() throws IOException, ParseException {
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			virtualEditionInter = (VirtualEditionInter) inter;
			break;
		}

		List<Property> properties = new ArrayList<Property>();
		properties.add(new TextProperty(1.0));

		List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
				new HashSet<VirtualEditionInter>(virtualEditionInters), properties);

		assertTrue(result.size() != 0);
	}

	@Test
	public void testGetMostSimilarItemForTextAsListOne() throws IOException, ParseException {
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			virtualEditionInter = (VirtualEditionInter) inter;
			break;
		}

		List<Property> properties = new ArrayList<Property>();
		properties.add(new TextProperty(1.0));

		List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
				new HashSet<VirtualEditionInter>(virtualEditionInters), properties);

		assertTrue(result.size() != 0);
	}

	@Test
	public void testGetMostSimilarItemForTextAsListTwo() throws IOException, ParseException {
		VirtualEditionInter virtualEditionInter = null;
		for (FragInter inter : virtualEdition.getIntersSet()) {
			virtualEditionInter = (VirtualEditionInter) inter;
			break;
		}

		List<Property> properties = new ArrayList<Property>();
		properties.add(new TextProperty(1.0));

		List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
				new HashSet<VirtualEditionInter>(virtualEditionInters), properties);

		assertTrue(result.size() != 0);

		result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
				new HashSet<VirtualEditionInter>(virtualEditionInters), properties);

		assertTrue(result.size() != 0);
	}

}
