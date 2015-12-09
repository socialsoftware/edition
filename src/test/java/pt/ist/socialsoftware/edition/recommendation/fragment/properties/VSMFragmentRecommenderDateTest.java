package pt.ist.socialsoftware.edition.recommendation.fragment.properties;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

@Ignore
public class VSMFragmentRecommenderDateTest extends VSMFragmentRecommenderStorableTest {
	private Double similiraty = 0.88;

	@Override
	protected Fragment getFragment1() {
		return ldod.getFragment("Fr084");
	}

	@Override
	protected Fragment getFragment2() {
		return ldod.getFragment("Fr078");
	}

	@Override
	protected Property getProperty() {
		return new DateProperty();
	}

	@Override
	protected Property getPropertyWithWeight() {
		return new DateProperty(2.0);
	}

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new DateProperty(.0);
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
