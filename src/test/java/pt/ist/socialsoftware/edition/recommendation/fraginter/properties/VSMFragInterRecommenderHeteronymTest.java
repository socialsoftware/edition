package pt.ist.socialsoftware.edition.recommendation.fraginter.properties;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

@Ignore
public class VSMFragInterRecommenderHeteronymTest extends VSMFragInterRecommenderStorableTest {

	@Override
	protected FragInter getFragment1() {
		List<FragInter> list = new ArrayList<>(ldod.getFragment("Fr002").getFragmentInterSet());
		for (FragInter inter : list) {
			if (inter.getEdition().getAcronym().equals("TSC"))
				return inter;
		}
		return list.get(list.size() - 1);
	}

	@Override
	protected FragInter getFragment2() {
		List<FragInter> list = new ArrayList<>(ldod.getFragment("Fr011").getFragmentInterSet());
		for (FragInter inter : list) {
			if (inter.getEdition().getAcronym().equals("TSC"))
				return inter;
		}
		return list.get(list.size() - 1);
	}

	@Override
	protected Property getProperty() {
		return new HeteronymProperty();
	}

	@Override
	protected Property getPropertyWithWeight() {
		return new HeteronymProperty(2.);
	}

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new HeteronymProperty(0.);
	}

	@Override
	@Test
	public void testCalculateSimiliraty() {
		double calculateSimiliraty = recommender.calculateSimiliraty(frag1, frag2, properties);
		Assert.assertEquals(0.0, calculateSimiliraty, DELTA);
	}

	@Override
	@Test
	public void testCalculateSimiliratyWithWeight() {
		double calculateSimiliraty = recommender.calculateSimiliraty(frag1, frag2, propertyWithWeight);
		Assert.assertEquals(0.0, calculateSimiliraty, DELTA);
	}
}
