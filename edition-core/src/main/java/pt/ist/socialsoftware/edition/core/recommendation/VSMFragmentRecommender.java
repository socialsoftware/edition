package pt.ist.socialsoftware.edition.core.recommendation;

import pt.ist.socialsoftware.edition.core.domain.Fragment;
import pt.ist.socialsoftware.edition.core.recommendation.properties.Property;

public class VSMFragmentRecommender extends VSMRecommender<Fragment> {

	@Override
	protected void prepareToLoadProperty(Fragment t1, Fragment t2, Property property) {
		property.prepareToLoadProperty(t1, t2);
	}

	@Override
	protected double[] loadProperty(Fragment t1, Property property) {
		return property.loadProperty(t1);
	}

}