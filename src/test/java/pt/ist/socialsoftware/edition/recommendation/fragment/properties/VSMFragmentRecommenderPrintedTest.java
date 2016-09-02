package pt.ist.socialsoftware.edition.recommendation.fragment.properties;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.recommendation.properties.PrintedProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

@Ignore
public class VSMFragmentRecommenderPrintedTest extends VSMFragmentRecommenderStorableTest {

	private double similiraty = 0.0;

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
		return new PrintedProperty();
	}

	@Override
	protected Property getPropertyWithWeight() {
		return new PrintedProperty(2.0);
	}

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new PrintedProperty(.0);
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

}
