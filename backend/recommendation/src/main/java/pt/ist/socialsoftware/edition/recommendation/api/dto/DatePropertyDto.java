package pt.ist.socialsoftware.edition.recommendation.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.DateProperty;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.Property;

public class DatePropertyDto extends PropertyDto {

    public DatePropertyDto(@JsonProperty("weight") String weight, @JsonProperty("acronym") String acronym) {
        super(Double.parseDouble(weight), acronym);
    }

    @Override
    public Property getProperty() {
        return new DateProperty(this.weight);
    }

    @Override
    public String getType() {
        return DATE;
    }

}
