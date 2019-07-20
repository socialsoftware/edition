package pt.ist.socialsoftware.edition.ldod.recommendation.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.Property;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.TextProperty;

public class TextPropertyDto extends PropertyDto {
    public TextPropertyDto(@JsonProperty("weight") String weight) {
        super(Double.parseDouble(weight));
    }

    @Override
    public Property getProperty() {
        return new TextProperty(this.weight);
    }
}
