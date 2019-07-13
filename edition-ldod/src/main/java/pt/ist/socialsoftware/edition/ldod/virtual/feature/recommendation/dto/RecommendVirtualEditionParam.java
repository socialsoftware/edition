package pt.ist.socialsoftware.edition.ldod.virtual.feature.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.recommendation.properties.Property;

import java.util.List;

public class RecommendVirtualEditionParam {
    private final String acronym;
    private final String id;
    private final List<Property> properties;

    public RecommendVirtualEditionParam(@JsonProperty("acronym") String acronym,
                                        @JsonProperty("id") String id,
                                        @JsonProperty("properties") List<Property> properties) {
        this.acronym = acronym;
        this.id = id;
        this.properties = properties;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public String getId() {
        return this.id;
    }

    public List<Property> getProperties() {
        return this.properties;
    }

}