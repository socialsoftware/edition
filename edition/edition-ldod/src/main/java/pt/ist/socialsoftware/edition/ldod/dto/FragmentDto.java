package pt.ist.socialsoftware.edition.ldod.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class FragmentDto {
	private String interId;
	private FragmentMetaInfoDto meta;
	String text;

	public FragmentDto(ExpertEditionInter inter, String text) {
		this.interId = inter.getExternalId();
		this.meta = new FragmentMetaInfoDto(inter);
		this.text = text;
	}

	public FragmentDto(VirtualEditionInter inter, String text) {
		this.interId = inter.getExternalId();
		this.meta = new FragmentMetaInfoDto(inter);
		this.text = text;
	}

	public String getInterId() {
		return this.interId;
	}

	public void setInterId(String interId) {
		this.interId = interId;
	}

	public FragmentMetaInfoDto getMeta() {
		return this.meta;
	}

	public void setMeta(FragmentMetaInfoDto meta) {
		this.meta = meta;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
