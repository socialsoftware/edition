package pt.ist.socialsoftware.edition.recommendation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SectionVirtualEditionParam {

	private final String acronym;
	private final List<PropertyWithLevel> properties;
	private final String id;

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

	public List<PropertyWithLevel> getProperties() {
		return properties;
	}

}
