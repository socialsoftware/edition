package pt.ist.socialsoftware.edition.recommendation.fragment.properties;

import org.junit.Assert;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.recommendation.properties.ManuscriptProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

public class VSMFragmentRecommenderManuscriptTest extends VSMFragmentRecommenderStorableTest {

	private double similiraty = 0.37;

	@Override
	protected Fragment getFragment1() {
		return ldod.getFragment("Fr011");
	}

	@Override
	protected Fragment getFragment2() {
		return ldod.getFragment("Fr506");
	}

	@Override
	protected Property getProperty() {
		return new ManuscriptProperty();
	}

	@Override
	protected Property getPropertyWithWeight() {
		return new ManuscriptProperty(2.0);
	}

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new ManuscriptProperty(.0);
	}

	@Override
	@Test
	public void testCalculateSimiliraty() {
		double calculateSimiliraty = vsmFragmentRecomender.calculateSimiliraty(frag1, frag2, properties);
		Assert.assertEquals(similiraty, calculateSimiliraty, DELTA);
	}

	@Override
	@Test
	public void testCalculateSimiliratyWithWeight() {
		double calculateSimiliraty = vsmFragmentRecomender.calculateSimiliraty(frag1, frag2, propertyWithWeight);
		Assert.assertEquals(similiraty, calculateSimiliraty, DELTA);
	}
}
