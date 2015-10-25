package pt.ist.socialsoftware.edition.recommendation.fraginter.properties;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.recommendation.properties.EditionProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

public class VSMFragInterRecommenderEditionTest extends VSMFragInterRecommenderStorableTest {

	private double similary = 0.9999999999999998;

	// @Override
	// protected Property getCompositeProperty() {
	// return new EditionProperty(2.0, 2.0, 2.0);
	// }

	@Override
	protected FragInter getFragment1() {
		List<FragInter> list = new ArrayList<>(ldod.getFragment("Fr084").getFragmentInterSet());
		for(FragInter inter : list) {
			if(inter.getEdition().getAcronym().equals("JPC"))
				return inter;
		}
		return list.get(list.size() - 1);
	}

	@Override
	protected FragInter getFragment2() {
		List<FragInter> list = new ArrayList<>(ldod.getFragment("Fr074").getFragmentInterSet());
		for(FragInter inter : list) {
			if(inter.getEdition().getAcronym().equals("JPC"))
				return inter;
		}
		return list.get(list.size() - 1);
	}

	@Override
	protected Property getProperty() {
		return new EditionProperty();
	}

	@Override
	protected Property getPropertyWithWeight() {
		return new EditionProperty(2.0);
	}

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new EditionProperty(0.);
	}

	@Override
	@Test
	public void testCalculateSimiliraty() {
		double calculateSimiliraty = recommender.calculateSimiliraty(frag1, frag2, properties);
		Assert.assertEquals(similary, calculateSimiliraty, DELTA);
	}

	@Override
	@Test
	public void testCalculateSimiliratyWithWeight() {
		double calculateSimiliraty = recommender.calculateSimiliraty(frag1, frag2, propertyWithWeight);
		Assert.assertEquals(similary, calculateSimiliraty, DELTA);
	}

}
