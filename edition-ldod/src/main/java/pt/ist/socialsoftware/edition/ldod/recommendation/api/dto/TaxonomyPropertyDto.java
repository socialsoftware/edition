package pt.ist.socialsoftware.edition.ldod.recommendation.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.Property;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.TaxonomyProperty;

public class TaxonomyPropertyDto extends PropertyDto {

    private final String acronym;

    public TaxonomyPropertyDto(@JsonProperty("weight") String weight, @JsonProperty("acronym") String acronym) {
        super(Double.parseDouble(weight));
        this.acronym = acronym;
    }

    @Override
    public Property getProperty() {
        return new TaxonomyProperty(this.weight, this.acronym);
    }
}
