package pt.ist.socialsoftware.edition.ldod.recommendation.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;

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
		return acronym;
	}

	public String getId() {
		return id;
	}

	public List<PropertyWithLevel> getPropertiesWithLevel() {
		return properties;
	}

	public List<Property> getProperties() {
		List<Property> result = new ArrayList<>();
		for (PropertyWithLevel propertyWithLevel : properties) {
			result.add(propertyWithLevel.getProperty());
		}
		return result;
	}

}
