package pt.ist.socialsoftware.edition.ldod.recommendation.feature;

import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.Property;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

public class VSMVirtualEditionInterRecommender extends VSMRecommender<VirtualEditionInterDto> {
    @Override
    protected void prepareToLoadProperty(VirtualEditionInterDto t1, VirtualEditionInterDto t2, Property property) {
        property.prepareToLoadProperty(t1, t2);
    }

    @Override
    protected double[] loadProperty(VirtualEditionInterDto t1, Property property) {
        return property.loadProperty(t1);
    }

}