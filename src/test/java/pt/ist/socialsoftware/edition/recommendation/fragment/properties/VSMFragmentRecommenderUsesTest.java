package pt.ist.socialsoftware.edition.recommendation.fragment.properties;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.recommendation.properties.UsesProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

@Ignore
public class VSMFragmentRecommenderUsesTest extends VSMFragmentRecommenderStorableTest {
	private double similiraty = 0.63;

	@Override
	protected Fragment getFragment1() {
		return ldod.getFragment("Fr084");
	}

	@Override
	protected Fragment getFragment2() {
		return ldod.getFragment("Fr002");
	}

	@Override
	protected Property getProperty() {
		return new UsesProperty();
	}

	@Override
	protected Property getPropertyWithWeight() {
		return new UsesProperty(2.0);
	}

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new UsesProperty(.0);
	}

	@Override
	@Test
	public void testCalculateSimiliraty() {
		double calculateSimiliraty = vsmFragmentRecomender.calculateSimilarity(frag1, frag2, properties);
		Assert.assertEquals(similiraty, calculateSimiliraty, DELTA);
	}

}
