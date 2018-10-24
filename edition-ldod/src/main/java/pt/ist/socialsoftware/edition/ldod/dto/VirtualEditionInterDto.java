package pt.ist.socialsoftware.edition.ldod.dto;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;

public class VirtualEditionInterDto {
	private String fragmentId;
	private String title;
	private int number;
	private String urlId;
	private String text;

	public VirtualEditionInterDto() {
	}

	public VirtualEditionInterDto(VirtualEditionInter inter) {
		this.setFragmentId(inter.getFragment().getXmlId());
		this.title = inter.getTitle();
		this.number = inter.getNumber();
		this.urlId = inter.getUrlId();

		PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed());
		writer.write(false);

		this.text = writer.getTranscription();
	}

	public String getFragmentId() {
		return this.fragmentId;
	}

	public void setFragmentId(String fragmentId) {
		this.fragmentId = fragmentId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getUrlId() {
		return this.urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
