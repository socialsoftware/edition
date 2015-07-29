package pt.ist.socialsoftware.edition.recommendation.fragment.properties;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import pt.ist.socialsoftware.edition.recommendation.StoredVectors;
import pt.ist.socialsoftware.edition.recommendation.properties.StorableProperty;

public abstract class VSMFragmentRecommenderStorableTest extends VSMFragmentRecommenderTest {

	@Test
	public void testGetStoredValue() {
		vsmFragmentRecomender.calculateSimiliraty(frag1, frag2, property);

		Collection<Double> collection1 = StoredVectors.getInstance().get((StorableProperty) property, frag1.getExternalId());
		Assert.assertNotNull(collection1);
		for(Double i : collection1) {
			if( i != 0.0 && i != 1.0 ){
				Assert.fail();
			}
		}

		Collection<Double> collection2 = StoredVectors.getInstance().get((StorableProperty) property, frag2.getExternalId());
		Assert.assertNotNull(collection2);
		for(Double i : collection2) {
			if(i != 0.0 && i != 1.0) {
				Assert.fail();
			}
		}
	}

	@Test
	public void testGetStoredValueWithWeight() {
		vsmFragmentRecomender.calculateSimiliraty(frag1, frag2, propertyWithWeight);
		for(Double d : StoredVectors.getInstance().get((StorableProperty) propertyWithWeight, frag1.getExternalId())) {
			Assert.assertTrue(d >= 0.0);
			Assert.assertTrue(d <= 1.00001);
		}
		for(Double d : StoredVectors.getInstance().get((StorableProperty) property, frag1.getExternalId())) {
			Assert.assertTrue(d >= 0.0);
			Assert.assertTrue(d <= 1.00001);
		}
		for(Double d : StoredVectors.getInstance().get((StorableProperty) propertyWithWeight, frag2.getExternalId())) {
			Assert.assertTrue(d >= 0.0);
			Assert.assertTrue(d <= 1.00001);
		}
		for(Double d : StoredVectors.getInstance().get((StorableProperty) property, frag2.getExternalId())) {
			Assert.assertTrue(d >= 0.0);
			Assert.assertTrue(d <= 1.00001);
		}
	}
}
