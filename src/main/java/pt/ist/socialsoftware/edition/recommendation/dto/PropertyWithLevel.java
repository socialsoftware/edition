package pt.ist.socialsoftware.edition.recommendation.dto;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import pt.ist.socialsoftware.edition.recommendation.properties.Property;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
 @Type(value = PropertyWithLevel.class, name = "property-with-level") })
public class PropertyWithLevel {
	private final int level;
	private final Property property;

	public PropertyWithLevel(
			@JsonProperty("level") int level, 
			@JsonProperty("property") Property property) {
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
