package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

public class PublicEditionListDto {
	private String acronym;
	private String title;

	public PublicEditionListDto(VirtualEdition vir) {
		this.setAcronym(vir.getAcronym());
		this.setTitle(vir.getTitle());
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
