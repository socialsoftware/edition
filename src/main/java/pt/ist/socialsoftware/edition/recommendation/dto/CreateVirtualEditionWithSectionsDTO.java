package pt.ist.socialsoftware.edition.recommendation.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class CreateVirtualEditionWithSectionsDTO extends VirtualEditionWithSectionsDTO {

	private final String title;
	private final boolean pub;
	
	public CreateVirtualEditionWithSectionsDTO(
			@JsonProperty("acronym") String acronym, 
			@JsonProperty("title") String title,
			@JsonProperty("pub") boolean pub, 
			@JsonProperty("sections") List<SectionDTO> sections) {
		super(acronym, sections);
		this.title = title;
		this.pub = pub;
	}

	public boolean getPub() {
		return pub;
	}

	public String getTitle() {
		return title;
	}

}
