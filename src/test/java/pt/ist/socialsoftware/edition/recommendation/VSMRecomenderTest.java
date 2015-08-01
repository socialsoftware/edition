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
			property = new HeteronymProperty();
			properties = new ArrayList<>();
			properties.add(property);
		} catch(WriteOnReadError | NotSupportedException | SystemException e1) {
			e1.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		try {
			FenixFramework.getTransactionManager().rollback();
		} catch(IllegalStateException | SecurityException | SystemException e) {
			e.printStackTrace();
		}
	}

	public static <P> void testFragments(List<Entry<P, Double>> getMostSimilarFragments) {
		double before = VSMFragmentRecommenderTest.MAX;
		for(Entry<P, Double> entrySet : getMostSimilarFragments) {
			Assert.assertTrue(before >= entrySet.getValue());
			before = entrySet.getValue();
		}
	}

	public static <P> void testFragments(List<Entry<P, Double>> getMostSimilarFragments, double min) {
		double before = VSMFragmentRecommenderTest.MAX;
		for(Entry<P, Double> entrySet : getMostSimilarFragments) {
			Assert.assertTrue(before >= entrySet.getValue());
			Assert.assertTrue(entrySet.getValue() >= min);
			before = entrySet.getValue();
		}
	}

	@Test
	public void testCalculateSimiliratyTTCollectionOfProperty() {
		recommender.calculateSimiliraty(f1, f2, properties);
	}

	@Test
	public void testCalculateSimiliratyTTProperty() {
		recommender.calculateSimiliraty(f1, f2, property);
	}

	@Test
	public void testGetMostSimilarItemTCollectionOfTCollectionOfProperty() {
		recommender.getMostSimilarItem(f1, items, properties);
	}

	@Test
	public void testGetMostSimilarItemTCollectionOfTProperty() {
		recommender.getMostSimilarItem(f1, items, property);
	}

	@Test
	public void testGetMostSimilarItemsTCollectionOfProperty() {
		List<Entry<T, Double>> mostSimilarItems = recommender.getMostSimilarItems(f1, properties);
		testFragments(mostSimilarItems);
	}

	@Test
	public void testGetMostSimilarItemsTCollectionOfPropertyDouble() {
		List<Entry<T, Double>> mostSimilarItems = recommender.getMostSimilarItems(f1, properties, MIN);
		testFragments(mostSimilarItems, MIN);
	}

	@Test
	public void testGetMostSimilarItemsTCollectionOfPropertyInt() {
		recommender.getMostSimilarItems(f1, properties, SIZE);
	}

	@Test
	public void testGetMostSimilarItemsTCollectionOfTCollectionOfProperty() {
		List<Entry<T, Double>> mostSimilarItems = recommender.getMostSimilarItems(f1, items, properties);
		Assert.assertEquals(items.size() - 1, mostSimilarItems.size());
		testFragments(mostSimilarItems);
	}

	@Test
	public void testGetMostSimilarItemsTCollectionOfTCollectionOfPropertyDouble() {
		List<Entry<T, Double>> mostSimilarItems = recommender.getMostSimilarItems(f1, items, properties, MIN);
		testFragments(mostSimilarItems, MIN);
	}

	@Test
	public void testGetMostSimilarItemsTCollectionOfTCollectionOfPropertyInt() {
		List<T> mostSimilarItems = recommender.getMostSimilarItems(f1, items, properties, SIZE);
		Assert.assertEquals(SIZE, mostSimilarItems.size());
	}

	@Test
	public void testGetMostSimilarItemsTCollectionOfTProperty() {
		List<Entry<T, Double>> mostSimilarItems = recommender.getMostSimilarItems(f1, items, property);
		Assert.assertEquals(items.size() - 1, mostSimilarItems.size());
		testFragments(mostSimilarItems);
	}

	@Test
	public void testGetMostSimilarItemsTCollectionOfTPropertyDouble() {
		List<Entry<T, Double>> mostSimilarItems = recommender.getMostSimilarItems(f1, items, property, MIN);
		testFragments(mostSimilarItems, MIN);
	}

	@Test
	public void testGetMostSimilarItemsTProperty() {
		List<Entry<T, Double>> mostSimilarItems = recommender.getMostSimilarItems(f1, property);
		testFragments(mostSimilarItems);
	}

	@Test
	public void testGetMostSimilarItemsTPropertyDouble() {
		List<Entry<T, Double>> mostSimilarItems = recommender.getMostSimilarItems(f1, property, MIN);
		testFragments(mostSimilarItems, MIN);
	}

	@Test
	public void testGetRecommendedEditionTCollectionOfProperty() {
		List<T> recommendedEdition = recommender.getRecommendedEdition(f1, properties);
		Assert.assertEquals(f1, recommendedEdition.get(0));
	}

	@Test
	public void testGetRecommendedEditionTCollectionOfTCollectionOfProperty() {
		List<T> recommendedEdition = recommender.getRecommendedEdition(f1, items, properties);
		Assert.assertEquals(f1, recommendedEdition.get(0));
		Assert.assertEquals(items.size(), recommendedEdition.size());
	}

	@Test
	public void testGetRecommendedEditionTCollectionOfTProperty() {
		List<T> recommendedEdition = recommender.getRecommendedEdition(f1, items, property);
		Assert.assertEquals(f1, recommendedEdition.get(0));
		Assert.assertEquals(items.size(), recommendedEdition.size());
	}

	@Test
	public void testGetRecommendedEditionTProperty() {
		List<T> recommendedEdition = recommender.getRecommendedEdition(f1, properties);
		Assert.assertEquals(f1, recommendedEdition.get(0));
	}

	@Test
	public void testGetRecommendedEditionWithRandomStartCollectionOfProperty() {
		recommender.getRecommendedEditionWithRandomStart(properties);
	}

	@Test
	public void testGetRecommendedEditionWithRandomStartProperty() {
		recommender.getRecommendedEditionWithRandomStart(property);
	}

}
