package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;

public class SourceInterSimpleDto {
	
	private String xmlId;
	private String urlId;
	private String title;

	public SourceInterSimpleDto(SourceInter sourceInter) {
		this.setXmlId(sourceInter.getFragment().getXmlId());
		this.setUrlId(sourceInter.getUrlId());
		this.setTitle(sourceInter.getTitle());
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

}
