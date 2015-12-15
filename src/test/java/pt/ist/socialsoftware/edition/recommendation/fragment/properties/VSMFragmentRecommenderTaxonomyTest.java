package pt.ist.socialsoftware.edition.recommendation.fragment.properties;

import org.junit.Ignore;

import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.TaxonomyProperty;

@Ignore
@Deprecated
public class VSMFragmentRecommenderTaxonomyTest extends VSMFragmentRecommenderTest {

	private Edition edition;

	@Override
	protected Fragment getFragment1() {
		return ldod.getFragment("Fr001");
	}

	@Override
	protected Fragment getFragment2() {
		return null;
	}

	@Override
	protected Property getProperty() {
		return new TaxonomyProperty();
	}

	@Override
	protected Property getPropertyWithWeight() {
		return new TaxonomyProperty(2.0);
	}

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new TaxonomyProperty(.0);
	}

	@Override
	protected void prepare() {
		edition = LdoD.getInstance().getEdition("afs");
	}

	@Override
	public void setUp() {
		super.setUp();
	}
}
