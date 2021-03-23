package pt.ist.socialsoftware.edition.recommendation.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.TaxonomyProperty;

public class TaxonomyPropertyDto extends PropertyDto {

    public TaxonomyPropertyDto(@JsonProperty("weight") String weight, @JsonProperty("acronym") String acronym) {
        super(Double.parseDouble(weight), acronym);
    }

    @Override
    public Property getProperty() {
        return new TaxonomyProperty(this.weight, this.acronym);
    }

    @Override
    public String getType() {
        return PropertyDto.TAXONOMY;
    }
}
