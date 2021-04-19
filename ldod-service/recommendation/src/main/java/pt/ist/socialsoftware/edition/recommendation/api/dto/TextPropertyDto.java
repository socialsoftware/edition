package pt.ist.socialsoftware.edition.recommendation.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.TextProperty;

public class TextPropertyDto extends PropertyDto {
    public TextPropertyDto(@JsonProperty("weight") String weight, @JsonProperty("acronym") String acronym) {
        super(Double.parseDouble(weight), acronym);
    }

    @Override
    public Property getProperty() {
        return new TextProperty(this.weight);
    }

    @Override
    public String getType() {
        return TEXT;
    }

}
