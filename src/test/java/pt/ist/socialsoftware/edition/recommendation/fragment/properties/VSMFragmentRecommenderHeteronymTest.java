package pt.ist.socialsoftware.edition.recommendation.fragment.properties;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

@Ignore
public class VSMFragmentRecommenderHeteronymTest extends VSMFragmentRecommenderStorableTest {

	private double similiraty = 0.8164965809277259;

	@Override
	protected Fragment getFragment1() {
		return ldod.getFragment("Fr002");
	}

	@Override
	protected Fragment getFragment2() {
		return ldod.getFragment("Fr011");
	}

	@Override
	protected Property getProperty() {
		return new HeteronymProperty();
	}

	@Override
	protected Property getPropertyWithWeight() {
		return new HeteronymProperty(2.0);
	}

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new HeteronymProperty(.0);
	}

	@Test
	public void testCalculateSimilarityWithAllHeteronyms() {
		for (double d : v2) {
			Assert.assertEquals(1.0, d, DELTA);
		}
	}

	@Override
	@Test
	public void testCalculateSimiliraty() {
		double calculateSimiliraty = vsmFragmentRecomender.calculateSimilarity(frag1, frag2, properties);
		Assert.assertEquals(similiraty, calculateSimiliraty, DELTA);
	}

}
