package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;

public class ExpertEditionDto {
	private String acronym;
	private String editor;
	private List<ExpertEditionInterDto> inter;
	private String externalId;
	

	public ExpertEditionDto(ExpertEdition expert) {
		this.setAcronym(expert.getAcronym());
		this.setEditor(expert.getEditor());
		this.setExternalId(expert.getExternalId());
	}

	public ExpertEditionDto(ExpertEdition expert, Fragment fragment) {
		this.setAcronym(expert.getAcronym());
		this.setEditor(expert.getEditor());
		this.setInter(expert.getSortedInter4Frag(fragment).stream().map(ExpertEditionInterDto::new).collect(Collectors.toList()));
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

	public List<ExpertEditionInterDto> getInter() {
		return inter;
	}

	public void setInter(List<ExpertEditionInterDto> inter) {
		this.inter = inter;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
}
