package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;

public class ExpertEditionDto {
	private String acronym;
	private String editor;

	public ExpertEditionDto(ExpertEdition expert) {
		this.setAcronym(expert.getAcronym());
		this.setEditor(expert.getEditor());
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}
}
