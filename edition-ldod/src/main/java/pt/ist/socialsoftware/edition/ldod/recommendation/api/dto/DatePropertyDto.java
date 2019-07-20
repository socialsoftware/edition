package pt.ist.socialsoftware.edition.ldod.recommendation.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.DateProperty;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.Property;

public class DatePropertyDto extends PropertyDto {

    public DatePropertyDto(@JsonProperty("weight") String weight) {
        super(Double.parseDouble(weight));
    }

    @Override
    public Property getProperty() {
        return new DateProperty(this.weight);
    }
}
