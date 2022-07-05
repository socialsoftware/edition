package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;

public class GameDto {
	private String virtualExternalId;
	private String externalId;
	private String virtualTitle;
	private String interTitle;

	public GameDto(ClassificationGame cg) {
		this.setVirtualExternalId(cg.getVirtualEdition().getExternalId());
		this.setExternalId(cg.getExternalId());
		this.setVirtualTitle(cg.getVirtualEdition().getTitle());
		this.setInterTitle(cg.getVirtualEditionInter().getTitle());
	}

	public String getVirtualExternalId() {
		return virtualExternalId;
	}

	public void setVirtualExternalId(String virtualExternalId) {
		this.virtualExternalId = virtualExternalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getVirtualTitle() {
		return virtualTitle;
	}

	public void setVirtualTitle(String virtualTitle) {
		this.virtualTitle = virtualTitle;
	}

	public String getInterTitle() {
		return interTitle;
	}

	public void setInterTitle(String interTitle) {
		this.interTitle = interTitle;
	}
}
