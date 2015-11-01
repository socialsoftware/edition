package pt.ist.socialsoftware.edition.recommendation.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.recommendation.properties.Property;

public class IterativeSortVirtualEditionParam {

    private final String acronym;
    private final List<PropertyWithLevel> properties;
    private final String id;

    public IterativeSortVirtualEditionParam(
            @JsonProperty("acronym") String acronym,
            @JsonProperty("id") String id,
            @JsonProperty("properties") List<PropertyWithLevel> properties) {
        this.acronym = acronym;
        this.properties = properties;
        this.id = id;
    }

    public String getAcronym() {
        return acronym;
    }

    public String getId() {
        return id;
    }

    public List<PropertyWithLevel> getProperties() {
        return properties;
    }

    public List<Property> getNormalizeProperties() {
        List<Property> properties = new ArrayList<Property>();
        for (PropertyWithLevel propertywithLevel : getProperties()) {
            properties.add(propertywithLevel.getProperty());
        }
        return properties;
    }

}
