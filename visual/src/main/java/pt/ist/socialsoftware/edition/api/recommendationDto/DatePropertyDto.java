package pt.ist.socialsoftware.edition.api.recommendationDto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class DatePropertyDto extends PropertyDto {

    public DatePropertyDto(@JsonProperty("weight") String weight, @JsonProperty("acronym") String acronym) {
        super(Double.parseDouble(weight), acronym);
    }


    @Override
    public String getType() {
        return DATE;
    }

}
