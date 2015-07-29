package pt.ist.socialsoftware.edition.recommendation.fragment.properties;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.recommendation.properties.DynamicDateProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

public class VSMFragmentRecommenderDynamicDateTest extends VSMFragmentRecommenderTest {

	@Override
	protected Fragment getFragment1() {
		return ldod.getFragment("Fr084");
	}

	@Override
	protected Fragment getFragment2() {
		return ldod.getFragment("Fr078");
	}

	@Override
	protected Property getProperty() {
		return new DynamicDateProperty();
	}

	@Override
	protected Property getPropertyWithWeight() {
		return new DynamicDateProperty(2.0);
	}

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new DynamicDateProperty(.0);
	}

}
