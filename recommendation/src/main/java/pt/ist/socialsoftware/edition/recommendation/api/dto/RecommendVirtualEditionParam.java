package pt.ist.socialsoftware.edition.recommendation.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RecommendVirtualEditionParam {
    private final String acronym;
    private final String id;
    private final List<PropertyDto> properties;

    public RecommendVirtualEditionParam(@JsonProperty("acronym") String acronym,
                                        @JsonProperty("id") String id,
                                        @JsonProperty("properties") List<PropertyDto> properties) {
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

    public List<PropertyDto> getProperties() {
        return this.properties;
    }

}