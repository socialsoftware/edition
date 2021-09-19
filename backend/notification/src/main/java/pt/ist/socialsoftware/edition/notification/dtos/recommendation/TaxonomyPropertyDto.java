package pt.ist.socialsoftware.edition.notification.dtos.recommendation;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TaxonomyPropertyDto extends PropertyDto {

    public TaxonomyPropertyDto(@JsonProperty("weight") String weight, @JsonProperty("acronym") String acronym) {
        super(Double.parseDouble(weight), acronym);
    }


    @Override
    public String getType() {
        return PropertyDto.TAXONOMY;
    }
}
