package pt.ist.socialsoftware.edition.ldod.virtual.feature.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.recommendation.properties.Property;

import java.util.ArrayList;
import java.util.List;

public class SectionVirtualEditionParam {
    private final String acronym;
    private final String id;
    private final List<PropertyWithLevel> properties;

    public SectionVirtualEditionParam(@JsonProperty("acronym") String acronym, @JsonProperty("id") String id,
									  @JsonProperty("properties") List<PropertyWithLevel> properties) {
        this.acronym = acronym;
        this.properties = properties;
        this.id = id;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public String getId() {
        return this.id;
    }

    public List<PropertyWithLevel> getPropertiesWithLevel() {
        return this.properties;
    }

    public List<Property> getProperties() {
        List<Property> result = new ArrayList<>();
        for (PropertyWithLevel propertyWithLevel : this.properties) {
            result.add(propertyWithLevel.getProperty());
        }
        return result;
    }

}
