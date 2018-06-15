package pt.ist.socialsoftware.edition.ldod.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = PropertyWithLevel.class, name = "property-with-level") })
public class PropertyWithLevel {
	private final int level;
	private final Property property;

	public PropertyWithLevel(@JsonProperty("level") int level, @JsonProperty("property") Property property) {
		this.level = level;
		this.property = property;
	}

	public Integer getLevel() {
		return level;
	}

	public Property getProperty() {
		return property;
	}

}
