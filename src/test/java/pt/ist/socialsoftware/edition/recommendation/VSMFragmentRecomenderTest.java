package pt.ist.socialsoftware.edition.recommendation;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.Fragment;

public class VSMFragmentRecomenderTest extends VSMRecomenderTest<Fragment> {

	@Override
	@Before
	public void setUp() {
		super.setUp();
		recommender = new VSMFragmentRecommender();
		f1 = ldod.getFragment("Fr001");
		f2 = ldod.getFragment("Fr002");
		items = ldod.getFragmentsSet();
	}

	@Test
	public void testCalculateSimilarity() {
		Collection<Double> v1 = f1.accept(property);
		Collection<Double> v2 = f2.accept(property);
		recommender.calculateSimilarity(v1, v2);
	}

}
