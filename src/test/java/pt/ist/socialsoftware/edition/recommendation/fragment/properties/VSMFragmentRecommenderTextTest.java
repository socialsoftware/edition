package pt.ist.socialsoftware.edition.recommendation.fragment.properties;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Assert;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.TextProperty;
import pt.ist.socialsoftware.edition.search.options.Indexer;

public class VSMFragmentRecommenderTextTest extends VSMFragmentRecommenderTest {

	private double similiraty = 0.07866711752438274;

	@Override
	protected Fragment getFragment1() {
		return ldod.getFragment("Fr001");
	}

	@Override
	protected Fragment getFragment2() {
		return ldod.getFragment("Fr190");
	}

	@Override
	protected Property getProperty() {
		return new TextProperty();
	}

	@Override
	protected Property getPropertyWithWeight() {
		return new TextProperty(2.0);
	}

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new TextProperty(.0);
	}

	@Override
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void test() throws IOException, ParseException {
		Set<Fragment> fragments = ldod.getFragmentsSet();
		for(Fragment fragment : fragments) {
			Collection<String> terms = (new Indexer()).getTerms(fragment, 20);
			Assert.assertFalse(terms.isEmpty());
		}
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
