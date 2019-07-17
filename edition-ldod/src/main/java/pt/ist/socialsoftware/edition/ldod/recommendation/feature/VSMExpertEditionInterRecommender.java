package pt.ist.socialsoftware.edition.ldod.recommendation.feature;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.Property;

public class VSMExpertEditionInterRecommender extends VSMRecommender<ExpertEditionInter> {
    @Override
    protected void prepareToLoadProperty(ExpertEditionInter t1, ExpertEditionInter t2, Property property) {
        property.prepareToLoadProperty(t1, t2);
    }

    @Override
    protected double[] loadProperty(ExpertEditionInter t1, Property property) {
        return property.loadProperty(t1);
    }

}
