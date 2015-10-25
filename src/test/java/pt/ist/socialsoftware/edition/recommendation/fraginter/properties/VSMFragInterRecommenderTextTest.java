package pt.ist.socialsoftware.edition.recommendation.fraginter.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Assert;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.TextProperty;
import pt.ist.socialsoftware.edition.search.Indexer;

public class VSMFragInterRecommenderTextTest extends VSMFragInterRecommenderTest {
	private double similarity = 0.027;

	@Override
	protected FragInter getFragment1() {
		List<FragInter> list = new ArrayList<>(ldod.getFragment("Fr043").getFragmentInterSet());
		for(FragInter inter : list) {
			if(inter.getEdition().getAcronym().equals("JPC"))
				return inter;
		}
		return list.get(list.size() - 1);
	}

	@Override
	protected FragInter getFragment2() {
		List<FragInter> list = new ArrayList<>(ldod.getFragment("Fr002").getFragmentInterSet());
		for(FragInter inter : list) {
			if(inter.getEdition().getAcronym().equals("JPC")) {
				return inter;
			}
		}
		return list.get(list.size() - 1);
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
		return new TextProperty(0.);
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

}
