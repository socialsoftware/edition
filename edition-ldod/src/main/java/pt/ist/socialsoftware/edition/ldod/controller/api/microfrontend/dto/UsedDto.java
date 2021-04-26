package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;

public class UsedDto {

	private String shortName;
	private String urlId;
	private String xmlId;

	public UsedDto(FragInter fragInter) {
		this.setXmlId(fragInter.getFragment().getXmlId());
		this.setUrlId(fragInter.getUrlId());
		this.setShortName(fragInter.getShortName());
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public String getXmlId() {
		return xmlId;
	}

	public void setXmlId(String xmlId) {
		this.xmlId = xmlId;
	}
}
