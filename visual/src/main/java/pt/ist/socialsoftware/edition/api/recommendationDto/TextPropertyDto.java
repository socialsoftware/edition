package pt.ist.socialsoftware.edition.api.recommendationDto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TextPropertyDto extends PropertyDto {
    public TextPropertyDto(@JsonProperty("weight") String weight, @JsonProperty("acronym") String acronym) {
        super(Double.parseDouble(weight), acronym);
    }

//    @Override
//    public Property getProperty() {
//        return new TextProperty(this.weight);
//    }

    @Override
    public String getType() {
        return TEXT;
    }

}
