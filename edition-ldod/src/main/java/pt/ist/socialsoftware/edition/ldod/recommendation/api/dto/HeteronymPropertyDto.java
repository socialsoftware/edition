package pt.ist.socialsoftware.edition.ldod.recommendation.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.Property;

public class HeteronymPropertyDto extends PropertyDto {

    public HeteronymPropertyDto(@JsonProperty("weight") String weight) {
        super(Double.parseDouble(weight));
    }

    @Override
    public Property getProperty() {
        return new HeteronymProperty(this.weight);
    }
}
