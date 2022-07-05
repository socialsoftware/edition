package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class VirtualEditionInterDto {
	private String externalId;
	private String fragmentXmlId;
	private String urlId;
	private int number;
	private String title;
	private List<FragInterDto> listUsed;

	public VirtualEditionInterDto(VirtualEditionInter vEditionInter) {
		this.setExternalId(vEditionInter.getExternalId());
		this.setFragmentXmlId(vEditionInter.getFragment().getXmlId());
		this.setUrlId(vEditionInter.getUrlId());
		this.setNumber(vEditionInter.getNumber());
		this.setTitle(vEditionInter.getTitle());
		this.setListUsed(vEditionInter.getListUsed().stream().map(FragInterDto::new).collect(Collectors.toList()));
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<FragInterDto> getListUsed() {
		return listUsed;
	}

	public void setListUsed(List<FragInterDto> listUsed) {
		this.listUsed = listUsed;
	}
}
