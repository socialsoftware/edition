package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class VirtualEditionInterDto {
	private String externalId;
	private String fragmentXmlId;
	private String urlId;
	private int number;

	public VirtualEditionInterDto(VirtualEditionInter vEditionInter) {
		this.setExternalId(vEditionInter.getExternalId());
		this.setFragmentXmlId(vEditionInter.getFragment().getXmlId());
		this.setUrlId(vEditionInter.getUrlId());
		this.setNumber(vEditionInter.getNumber());
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getFragmentXmlId() {
		return fragmentXmlId;
	}

	public void setFragmentXmlId(String fragmentXmlId) {
		this.fragmentXmlId = fragmentXmlId;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
