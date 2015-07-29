package pt.ist.socialsoftware.edition.recommendation.fraginter.properties;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.TypescriptProperty;

public class VSMFragInterRecommenderTypescriptTest extends VSMFragInterRecommenderStorableTest {

	private double similarity = 0.9999999999999996;

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new TypescriptProperty(0.);
	}

	@Override
	protected FragInter getFragment1() {
		List<FragInter> list = new ArrayList<>(ldod.getFragment("Fr097").getFragmentInterSet());
		for(FragInter inter : list) {
			if(inter instanceof SourceInter)
				return inter;
		}
		return list.get(list.size() - 1);
	}

	@Override
	protected FragInter getFragment2() {
		List<FragInter> list = new ArrayList<>(ldod.getFragment("Fr008").getFragmentInterSet());
		for(FragInter inter : list) {
			if(inter instanceof SourceInter)
				return inter;
		}
		return list.get(list.size() - 1);
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
	public void setUp() {
		super.setUp();
	}

	@Override
	@Test
	public void testCalculateSimiliraty() {
		double calculateSimiliraty = recommender.calculateSimiliraty(frag1, frag2, properties);
		Assert.assertEquals(similarity, calculateSimiliraty, DELTA);
	}

	@Override
	@Test
	public void testCalculateSimiliratyWithWeight() {
		double calculateSimiliraty = recommender.calculateSimiliraty(frag1, frag2, propertyWithWeight);
		Assert.assertEquals(similarity, calculateSimiliraty, DELTA);
	}
}
