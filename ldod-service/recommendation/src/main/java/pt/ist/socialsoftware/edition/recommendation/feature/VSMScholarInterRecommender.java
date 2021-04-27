package pt.ist.socialsoftware.edition.recommendation.feature;

import pt.ist.socialsoftware.edition.recommendation.feature.properties.Property;
import pt.ist.socialsoftware.edition.virtual.api.textDto.ScholarInterDto;


public class VSMScholarInterRecommender extends VSMRecommender<ScholarInterDto> {
    @Override
    protected void prepareToLoadProperty(ScholarInterDto t1, ScholarInterDto t2, Property property) {
        property.prepareToLoadProperty(t1, t2);
    }

    @Override
    protected double[] loadProperty(ScholarInterDto t1, Property property) {
        return property.loadProperty(t1);
    }

}
