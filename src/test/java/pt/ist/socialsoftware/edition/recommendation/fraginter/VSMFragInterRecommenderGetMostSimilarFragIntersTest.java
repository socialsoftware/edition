package pt.ist.socialsoftware.edition.recommendation.fraginter;

import java.util.ArrayList;
import java.util.Collection;
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
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.recommendation.VSMFragInterRecommender;
import pt.ist.socialsoftware.edition.recommendation.VSMRecommender;
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

public class VSMFragInterRecommenderGetMostSimilarFragIntersTest {
	protected List<Property> properties;
	protected VSMRecommender<FragInter> recomender;
	private FragInter fragInter;
	private double treshold;
	private long time;
	private List<Entry<FragInter, Double>> getMostSimilarFragInter;
	private Collection<FragInter> inters;

	@Before
	public void setUp() {
		Bootstrap.initDatabase();
		try {
			FenixFramework.getTransactionManager().begin(false);
		} catch(WriteOnReadError | NotSupportedException | SystemException e1) {
			e1.printStackTrace();
		}
		LdoD ldod = LdoD.getInstance();
		fragInter = (new ArrayList<>(ldod.getFragment("Fr001").getFragmentInterSet())).get(0);
		properties = new ArrayList<Property>();
		inters = new ArrayList<>(ldod.getFragment("Fr001").getFragmentInterSet());
		properties.add(new EditionProperty());
		properties.add(new ManuscriptProperty());
		properties.add(new TypescriptProperty());
		properties.add(new PrintedProperty());
		properties.add(new HeteronymProperty());
		properties.add(new DateProperty());
		properties.add(new TextProperty());
		treshold = .8;
		recomender = new VSMFragInterRecommender();
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

	private void test(List<Entry<FragInter, Double>> getMostSimilarFragments) {
		double before = VSMFragmentRecommenderTest.MAX;
		for(Entry<FragInter, Double> entrySet : getMostSimilarFragments) {
			Assert.assertTrue(before >= entrySet.getValue());
			before = entrySet.getValue();
		}
	}

	@Test
	public final void testGetMostSimilarFragmentByDate() {
		getMostSimilarFragInter = recomender.getMostSimilarItems(fragInter, inters, new DateProperty());
		Assert.assertEquals(inters.size() - 1, getMostSimilarFragInter.size());
		test(getMostSimilarFragInter);
	}

	@Test
	public final void testGetMostSimilarFragmentByEdition() {
		getMostSimilarFragInter = recomender.getMostSimilarItems(fragInter, inters, new EditionProperty());
		Assert.assertEquals(inters.size() - 1, getMostSimilarFragInter.size());
		test(getMostSimilarFragInter);
	}

	@Test
	public final void testGetMostSimilarFragmentByHeteronym() {
		getMostSimilarFragInter = recomender.getMostSimilarItems(fragInter, inters, new HeteronymProperty());
		Assert.assertEquals(inters.size() - 1, getMostSimilarFragInter.size());
		test(getMostSimilarFragInter);
	}

	@Test
	public final void testGetMostSimilarFragmentByManuscript() {
		getMostSimilarFragInter = recomender.getMostSimilarItems(fragInter, inters, new ManuscriptProperty());
		Assert.assertEquals(inters.size() - 1, getMostSimilarFragInter.size());
		test(getMostSimilarFragInter);
	}

	@Test
	public final void testGetMostSimilarFragmentByTaxonomy() {
		getMostSimilarFragInter = recomender.getMostSimilarItems(fragInter, inters, new TaxonomyProperty());
		Assert.assertEquals(inters.size() - 1, getMostSimilarFragInter.size());
		test(getMostSimilarFragInter);
	}

	@Test
	public final void testGetMostSimilarFragmentByText() {
		getMostSimilarFragInter = recomender.getMostSimilarItems(fragInter, inters, new TextProperty());
		Assert.assertEquals(inters.size() - 1, getMostSimilarFragInter.size());
		test(getMostSimilarFragInter);
	}

	@Test
	public final void testGetMostSimilarFragmentByTypescript() {
		getMostSimilarFragInter = recomender.getMostSimilarItems(fragInter, inters, new TypescriptProperty());
		Assert.assertEquals(inters.size() - 1, getMostSimilarFragInter.size());
		test(getMostSimilarFragInter);
	}

	@Test
	public final void testGetMostSimilarFragmentsWithTreshold() {
		getMostSimilarFragInter = recomender.getMostSimilarItems(fragInter, inters, properties, treshold);
		test(getMostSimilarFragInter);
	}

	@Test
	public final void testgetMostSimilarItems() {
		getMostSimilarFragInter = recomender.getMostSimilarItems(fragInter, inters, properties);
		Assert.assertEquals(inters.size() - 1, getMostSimilarFragInter.size());
		Assert.assertFalse(getMostSimilarFragInter.isEmpty());
	}
}