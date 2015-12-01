package pt.ist.socialsoftware.edition.recommendation.fraginter.properties;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.recommendation.properties.PrintedProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

public class VSMFragInterRecommenderPrintedTest extends VSMFragInterRecommenderStorableTest {

	@Override
	protected FragInter getFragment1() {
		List<FragInter> list = new ArrayList<>(ldod.getFragment("Fr084").getFragmentInterSet());
		for (FragInter inter : list) {
			if (inter instanceof SourceInter && inter.getSourceType().equals(SourceType.PRINTED))
				return inter;
		}
		return list.get(list.size() - 1);
	}

	@Override
	protected FragInter getFragment2() {
		List<FragInter> list = new ArrayList<>(ldod.getFragment("Fr002").getFragmentInterSet());
		for (FragInter inter : list) {
			if (inter instanceof SourceInter && inter.getSourceType().equals(SourceType.PRINTED)) {
				return inter;
			}
		}
		return list.get(list.size() - 1);
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
		return new PrintedProperty(0.);
	}

	@Override
	public void setUp() {
		super.setUp();
	}

	@Override
	@Test
	public void testCalculateSimiliraty() {
		double calculateSimiliraty = recommender.calculateSimiliraty(frag1, frag2, properties);
		Assert.assertEquals(0.0, calculateSimiliraty, DELTA);
	}

	@Override
	@Test
	public void testCalculateSimiliratyAEqualsA() {
		double calculateSimiliraty = recommender.calculateSimiliraty(frag1, frag1, property);
		Assert.assertEquals(0.0, calculateSimiliraty, 0.0000001);
	}

	@Override
	@Test
	public void testCalculateSimiliratyWithWeight() {
		double calculateSimiliraty = recommender.calculateSimiliraty(frag1, frag2, propertyWithWeight);
		Assert.assertEquals(0.0, calculateSimiliraty, DELTA);
	}
}
