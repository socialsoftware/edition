package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;

public class FragInterShortDto {
	private String xmlId;
	private String urlId;
	private String title;
	private String externalId;
	
	public FragInterShortDto(FragInter fragInter) {
		this.setXmlId(fragInter.getFragment().getXmlId());
		this.setUrlId(fragInter.getUrlId());
		if(fragInter.getTitle() != null) {
			this.setTitle(fragInter.getTitle());
		}
		this.setExternalId(fragInter.getExternalId());
	}
	
	
	public String getXmlId() {
		return xmlId;
	}

	public void setXmlId(String xmlId) {
		this.xmlId = xmlId;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

}
