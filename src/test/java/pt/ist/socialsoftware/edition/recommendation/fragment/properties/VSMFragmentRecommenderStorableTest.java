package pt.ist.socialsoftware.edition.recommendation.fragment.properties;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.socialsoftware.edition.recommendation.StoredVectors;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.StorableProperty;

@Ignore
public abstract class VSMFragmentRecommenderStorableTest extends VSMFragmentRecommenderTest {

	@Test
	public void testGetStoredValueWithWeight() {
		Set<Property> properties = new HashSet<Property>();
		properties.add(propertyWithWeight);
		vsmFragmentRecomender.calculateSimilarity(frag1, frag2, properties);
		for (Double d : StoredVectors.getInstance().get((StorableProperty) propertyWithWeight, frag1.getExternalId())) {
			Assert.assertTrue(d >= 0.0);
			Assert.assertTrue(d <= 1.00001);
		}
		for (Double d : StoredVectors.getInstance().get((StorableProperty) property, frag1.getExternalId())) {
			Assert.assertTrue(d >= 0.0);
			Assert.assertTrue(d <= 1.00001);
		}
		for (Double d : StoredVectors.getInstance().get((StorableProperty) propertyWithWeight, frag2.getExternalId())) {
			Assert.assertTrue(d >= 0.0);
			Assert.assertTrue(d <= 1.00001);
		}
		for (Double d : StoredVectors.getInstance().get((StorableProperty) property, frag2.getExternalId())) {
			Assert.assertTrue(d >= 0.0);
			Assert.assertTrue(d <= 1.00001);
		}
	}
}
