package pt.ist.socialsoftware.edition.recommendation.fraginter.properties;

import org.junit.Assert;
import org.junit.Test;

import pt.ist.socialsoftware.edition.recommendation.StoredVectors;
import pt.ist.socialsoftware.edition.recommendation.properties.StorableProperty;

public abstract class VSMFragInterRecommenderStorableTest extends VSMFragInterRecommenderTest {

	@Test
	public void testGetStoredValue() {
		recommender.calculateSimiliraty(frag1, frag2, property);
		Assert.assertNotNull(StoredVectors.getInstance().get((StorableProperty) property, frag1.getExternalId()));
		Assert.assertNotNull(StoredVectors.getInstance().get((StorableProperty) property, frag2.getExternalId()));
	}

}
