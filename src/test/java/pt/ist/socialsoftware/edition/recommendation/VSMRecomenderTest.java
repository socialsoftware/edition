package pt.ist.socialsoftware.edition.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.recommendation.fragment.properties.VSMFragmentRecommenderTest;
import pt.ist.socialsoftware.edition.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.utils.Bootstrap;

@Ignore
public class VSMRecomenderTest<T> {

	private static final double MIN = 0.3;
	private static final int SIZE = 5;
	protected VSMRecommender<T> recommender;
	protected Property property;
	protected List<Property> properties;
	protected T f1;
	protected T f2;
	protected Collection<T> items;
	protected LdoD ldod;

	@Before
	public void setUp() {
		Bootstrap.initDatabase();
		try {
			FenixFramework.getTransactionManager().begin(false);
			ldod = LdoD.getInstance();
			property = new HeteronymProperty(1.0);
			properties = new ArrayList<>();
			properties.add(property);
		} catch (WriteOnReadError | NotSupportedException | SystemException e1) {
			e1.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		try {
			FenixFramework.getTransactionManager().rollback();
		} catch (IllegalStateException | SecurityException | SystemException e) {
			e.printStackTrace();
		}
	}

	public static <P> void testFragments(List<Entry<P, Double>> getMostSimilarFragments) {
		double before = VSMFragmentRecommenderTest.MAX;
		for (Entry<P, Double> entrySet : getMostSimilarFragments) {
			Assert.assertTrue(before >= entrySet.getValue());
			before = entrySet.getValue();
		}
	}

	public static <P> void testFragments(List<Entry<P, Double>> getMostSimilarFragments, double min) {
		double before = VSMFragmentRecommenderTest.MAX;
		for (Entry<P, Double> entrySet : getMostSimilarFragments) {
			Assert.assertTrue(before >= entrySet.getValue());
			Assert.assertTrue(entrySet.getValue() >= min);
			before = entrySet.getValue();
		}
	}

	@Test
	public void testCalculateSimiliratyTTCollectionOfProperty() {
		recommender.calculateSimilarity(f1, f2, properties);
	}

	@Test
	public void testGetMostSimilarItemTCollectionOfTCollectionOfProperty() {
		recommender.getMostSimilarItem(f1, items, properties);
	}

	@Test
	public void testGetMostSimilarItemsTCollectionOfTCollectionOfProperty() {
		List<Entry<T, Double>> mostSimilarItems = recommender.getMostSimilarItems(f1, items, properties);
		Assert.assertEquals(items.size(), mostSimilarItems.size());
		testFragments(mostSimilarItems);
	}

}
