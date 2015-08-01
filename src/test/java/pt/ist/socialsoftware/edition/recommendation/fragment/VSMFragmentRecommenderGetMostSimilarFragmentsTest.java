package pt.ist.socialsoftware.edition.recommendation.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.recommendation.VSMFragmentRecommender;
import pt.ist.socialsoftware.edition.recommendation.fragment.properties.VSMFragmentRecommenderTest;
import pt.ist.socialsoftware.edition.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.EditionProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.ManuscriptProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.PrintedProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.TaxonomyProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TextProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TypescriptProperty;
import pt.ist.socialsoftware.edition.utils.Bootstrap;

public class VSMFragmentRecommenderGetMostSimilarFragmentsTest {
	protected List<Property> properties;
	protected VSMFragmentRecommender recommender;
	private Fragment fragment;
	private double treshold;
	private long time;

	@Before
	public void setUp() {
		Bootstrap.initDatabase();
		try {
			FenixFramework.getTransactionManager().begin(false);
		} catch(WriteOnReadError | NotSupportedException | SystemException e1) {
			e1.printStackTrace();
		}
		LdoD ldod = LdoD.getInstance();
		fragment = ldod.getFragment("Fr001");
		properties = new ArrayList<Property>();
		properties.add(new EditionProperty(2.0));
		properties.add(new ManuscriptProperty());
		properties.add(new TypescriptProperty());
		properties.add(new PrintedProperty());
		properties.add(new HeteronymProperty());
		properties.add(new DateProperty());
		properties.add(new TextProperty());
		properties.add(new TaxonomyProperty());
		treshold = .8;
		recommender = new VSMFragmentRecommender();
		time = System.currentTimeMillis();
	}

	@After
	public void tearDown() throws Exception {
		Double duration = ((System.currentTimeMillis() - time) / 1000.0);
		System.out.println("Time: " + duration + "s");

		try {
			FenixFramework.getTransactionManager().rollback();
		} catch(IllegalStateException | SecurityException | SystemException e) {
			e.printStackTrace();
		}
	}

	private void testFragments(List<Entry<Fragment, Double>> getMostSimilarFragments) {
		double before = VSMFragmentRecommenderTest.MAX;
		for(Entry<Fragment, Double> entrySet : getMostSimilarFragments) {
			Assert.assertTrue(before >= entrySet.getValue());
			before = entrySet.getValue();
		}
	}

	@Test
	public final void testGetMostSimilarFragmentByDate() {
		List<Entry<Fragment, Double>> getMostSimilarFragments = recommender.getMostSimilarItems(fragment, new DateProperty());
		testFragments(getMostSimilarFragments);
	}

	@Test
	public final void testGetMostSimilarFragmentByEdition() {
		List<Entry<Fragment, Double>> getMostSimilarFragments = recommender.getMostSimilarItems(fragment, new EditionProperty());
		testFragments(getMostSimilarFragments);
	}

	@Test
	public final void testGetMostSimilarFragmentByHeteronym() {
		List<Entry<Fragment, Double>> getMostSimilarFragments = recommender.getMostSimilarItems(fragment, new HeteronymProperty());
		testFragments(getMostSimilarFragments);
	}

	@Test
	public final void testGetMostSimilarFragmentByManuscript() {
		List<Entry<Fragment, Double>> getMostSimilarFragments = recommender.getMostSimilarItems(fragment, new ManuscriptProperty());
		testFragments(getMostSimilarFragments);
	}

	@Test
	public final void testGetMostSimilarFragmentByTaxonomy() {
		List<Entry<Fragment, Double>> getMostSimilarFragments = recommender.getMostSimilarItems(fragment, new TaxonomyProperty());
		testFragments(getMostSimilarFragments);
	}

	@Test
	public final void testGetMostSimilarFragmentByText() {
		List<Entry<Fragment, Double>> getMostSimilarFragments = recommender.getMostSimilarItems(fragment, new TextProperty(), treshold);
		testFragments(getMostSimilarFragments);
	}

	@Test
	public final void testGetMostSimilarFragmentByTypescript() {
		List<Entry<Fragment, Double>> getMostSimilarFragments = recommender.getMostSimilarItems(fragment, new TypescriptProperty());
		testFragments(getMostSimilarFragments);
	}

	@Test
	public final void testGetMostSimilarFragmentsWithTreshold() {
		List<Entry<Fragment, Double>> getMostSimilarFragments = recommender.getMostSimilarItems(fragment, properties, treshold);
		testFragments(getMostSimilarFragments);
	}

	@Test
	public final void testgetMostSimilarItems() {
		List<Entry<Fragment, Double>> getRecommendedEdition = recommender.getMostSimilarItems(fragment, properties);
		Assert.assertFalse(getRecommendedEdition.isEmpty());

	}
}