package pt.ist.socialsoftware.edition.ldod.recommendation.feature;

import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.Property;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;

public class VSMFragmentRecommender extends VSMRecommender<FragmentDto> {

    @Override
    protected void prepareToLoadProperty(FragmentDto t1, FragmentDto t2, Property property) {
        property.prepareToLoadProperty(t1, t2);
    }

    @Override
    protected double[] loadProperty(FragmentDto t1, Property property) {
        return property.loadProperty(t1);
    }

}