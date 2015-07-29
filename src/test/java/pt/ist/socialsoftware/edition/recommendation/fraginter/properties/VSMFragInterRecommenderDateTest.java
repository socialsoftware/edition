package pt.ist.socialsoftware.edition.recommendation.fraginter.properties;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

public class VSMFragInterRecommenderDateTest extends VSMFragInterRecommenderStorableTest {

	private double similiraty = 0.9999999999999996;

	@Override
	protected FragInter getFragment1() {
		List<FragInter> list = new ArrayList<>(ldod.getFragment("Fr535").getFragmentInterSet());
		for(FragInter inter : list) {
			if(inter.getEdition().getAcronym().equals("JP"))
				return inter;
		}
		return list.get(list.size() - 1);
	}

	@Override
	protected FragInter getFragment2() {
		List<FragInter> list = new ArrayList<>(ldod.getFragment("Fr567").getFragmentInterSet());
		for(FragInter inter : list) {
			if(inter.getEdition().getAcronym().equals("JP"))
				return inter;
		}
		return list.get(list.size() - 1);
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
		return new DateProperty(0.);
	}

	@Override
	@Test
	public void testCalculateSimiliraty() {
		double calculateSimiliraty = recommender.calculateSimiliraty(frag1, frag2, properties);
		Assert.assertEquals(similiraty, calculateSimiliraty, DELTA);
	}

	@Override
	@Test
	public void testCalculateSimiliratyWithWeight() {
		double calculateSimiliraty = recommender.calculateSimiliraty(frag1, frag2, propertyWithWeight);
		Assert.assertEquals(similiraty, calculateSimiliraty, DELTA);
	}

}
