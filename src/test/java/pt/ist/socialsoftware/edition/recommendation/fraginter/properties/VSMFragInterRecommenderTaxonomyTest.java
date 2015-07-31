package pt.ist.socialsoftware.edition.recommendation.fraginter.properties;

import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.TaxonomyProperty;

public class VSMFragInterRecommenderTaxonomyTest extends VSMFragInterRecommenderTest {

	private Edition edition;

	@Override
	protected FragInter getFragment1() {
		return edition.getSortedInterps().get(1);
	}

	@Override
	protected FragInter getFragment2() {
		return edition.getSortedInterps().get(0);
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
		return new TaxonomyProperty(0.);
	}

	@Override
	protected void prepare() {
		edition = LdoD.getInstance().getEdition("andre");
	}

	@Override
	public void setUp() {
		super.setUp();
	}
}
