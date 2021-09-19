package pt.ist.socialsoftware.edition.recommendation.feature;

import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.Property;

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