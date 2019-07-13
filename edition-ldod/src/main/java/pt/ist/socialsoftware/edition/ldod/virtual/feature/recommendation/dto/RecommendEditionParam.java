package pt.ist.socialsoftware.edition.ldod.virtual.feature.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.recommendation.properties.Property;

import java.util.List;

public class RecommendEditionParam {
    private static final String FRAGMENT = "fragment";
    private static final String PROPERTIES = "properties";

    private final String id;
    private final List<Property> properties;

    public RecommendEditionParam(@JsonProperty(FRAGMENT) String id,
                                 @JsonProperty(PROPERTIES) List<Property> properties) {
        this.id = id;
        this.properties = properties;
    }

    public String getId() {
        return this.id;
    }

    public List<Property> getProperties() {
        return this.properties;
    }

}
