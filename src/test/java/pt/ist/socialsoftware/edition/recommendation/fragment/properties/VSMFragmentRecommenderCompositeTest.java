package pt.ist.socialsoftware.edition.recommendation.fragment.properties;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.socialsoftware.edition.recommendation.properties.Property;

@Ignore
public abstract class VSMFragmentRecommenderCompositeTest extends VSMFragmentRecommenderStorableTest {

	protected Property compositeProperty;

	protected abstract Property getCompositeProperty();

	@Override
	public void setUp() {
		super.setUp();
		compositeProperty = getCompositeProperty();
	}

	@Test
	public void testCalculateSimiliratyWithCompositeWeight() {
		double calculateSimiliraty = vsmFragmentRecomender.calculateSimilarity(frag1, frag2, compositeProperty);
		Assert.assertTrue(calculateSimiliraty >= 0);
		Assert.assertTrue(calculateSimiliraty <= 1.0000000000000002);
	}

}
