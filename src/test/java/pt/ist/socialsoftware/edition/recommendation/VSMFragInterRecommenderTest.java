package pt.ist.socialsoftware.edition.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

@Ignore
public class VSMFragInterRecommenderTest extends VSMRecomenderTest<FragInter> {

	private VSMFragInterRecommender fragInterRecommeder;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		recommender = fragInterRecommeder = new VSMFragInterRecommender();
		List<FragInter> arrayList = new ArrayList<>(ldod.getEdition("TSC").getIntersSet());
		f1 = arrayList.get(0);
		f2 = arrayList.get(1);
		items = arrayList;
	}

	@Test
	public void testGetClusteredEdition() {
		Map<Integer, Collection<Property>> propertiesMap = new HashMap<Integer, Collection<Property>>();
		propertiesMap.put(0, properties);
		fragInterRecommeder.getClusteredEdition(f1, items, propertiesMap);
	}

	@Test
	public void testGetDefaultSet() {
		recommender.getDefaultSet();
	}

	@Test
	public void testGetGlusteredEdition() {
		Map<Integer, Collection<Property>> propertiesMap = new HashMap<Integer, Collection<Property>>();
		propertiesMap.put(0, properties);
		fragInterRecommeder.getClusteredEdition(f1, propertiesMap);
	}

	@Test
	public void testGetRecommendedEditionWithoutDuplicatedFragmentsFragInterCollectionOfFragInterCollectionOfProperty() {
		fragInterRecommeder.getRecommendedEditionWithoutDuplicatedFragments(f1, items, properties);
	}

	@Test
	public void testGetRecommendedEditionWithoutDuplicatedFragmentsFragInterCollectionOfProperty() {
		fragInterRecommeder.getRecommendedEditionWithoutDuplicatedFragments(f1, properties);
	}

	@Test
	public void testGetRecommendedEditionWithoutDuplicatedFragmentsFragInterProperty() {
		fragInterRecommeder.getRecommendedEditionWithoutDuplicatedFragments(f1, property);
	}

	@Test
	public void testGetRecommendedEditionWithoutDuplicatedFragmentsFragInterCollectionOfFragInterProperty() {
		fragInterRecommeder.getRecommendedEditionWithoutDuplicatedFragments(f1, items, property);
	}

	@Test
	public void testCalculateSimilarity() {
		Collection<Double> v1 = f1.accept(property);
		Collection<Double> v2 = f2.accept(property);
		recommender.calculateSimilarity(v1, v2);
	}

}
