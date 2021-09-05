package pt.ist.socialsoftware.edition.notification.dtos.recommendation;

import com.fasterxml.jackson.annotation.JsonProperty;


public class HeteronymPropertyDto extends PropertyDto {

    public HeteronymPropertyDto(@JsonProperty("weight") String weight, @JsonProperty("acronym") String acronym) {
        super(Double.parseDouble(weight), acronym);
    }

//    @Override
//    public Property getProperty() {
//        return new HeteronymProperty(this.weight);
//    }

    @Override
    public String getType() {
        return HETERONYM;
    }

}
