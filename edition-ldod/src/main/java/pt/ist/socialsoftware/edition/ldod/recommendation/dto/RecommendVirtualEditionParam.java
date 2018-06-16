package pt.ist.socialsoftware.edition.ldod.recommendation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;

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
        return acronym;
    }

    public String getId() {
        return id;
    }

    public List<Property> getProperties() {
        return properties;
    }

}