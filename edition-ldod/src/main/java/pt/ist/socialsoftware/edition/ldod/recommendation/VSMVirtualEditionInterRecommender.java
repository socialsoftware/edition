package pt.ist.socialsoftware.edition.ldod.recommendation;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;

public class VSMVirtualEditionInterRecommender extends VSMRecommender<VirtualEditionInter> {
	@Override
	protected void prepareToLoadProperty(VirtualEditionInter t1, VirtualEditionInter t2, Property property) {
		property.prepareToLoadProperty(t1, t2);
	}

	@Override
	protected double[] loadProperty(VirtualEditionInter t1, Property property) {
		return property.loadProperty(t1);
	}

}