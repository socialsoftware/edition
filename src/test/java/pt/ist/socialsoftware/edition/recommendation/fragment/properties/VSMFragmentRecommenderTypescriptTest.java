package pt.ist.socialsoftware.edition.recommendation.fragment.properties;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.TypescriptProperty;

@Ignore
public class VSMFragmentRecommenderTypescriptTest extends VSMFragmentRecommenderStorableTest {
	private double similiraty = 0.37;

	@Override
	protected Fragment getFragment1() {
		return ldod.getFragment("Fr014");
	}

	@Override
	protected Fragment getFragment2() {
		return ldod.getFragment("Fr008");
	}

	@Override
	protected Property getProperty() {
		return new TypescriptProperty();
	}

	@Override
	protected Property getPropertyWithWeight() {
		return new TypescriptProperty(2.0);
	}

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new TypescriptProperty(.0);
	}

	@Override
	public void setUp() {
		super.setUp();
	}

	@Override
	@Test
	public void testCalculateSimiliraty() {
		double calculateSimiliraty = vsmFragmentRecomender.calculateSimilarity(frag1, frag2, properties);
		Assert.assertEquals(similiraty, calculateSimiliraty, DELTA);
	}

	@Override
	@Test
	public void testCalculateSimiliratyWithWeight() {
		double calculateSimiliraty = vsmFragmentRecomender.calculateSimiliraty(frag1, frag2, propertyWithWeight);
		Assert.assertEquals(similiraty, calculateSimiliraty, DELTA);
	}

}
