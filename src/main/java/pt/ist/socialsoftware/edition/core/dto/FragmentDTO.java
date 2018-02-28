package pt.ist.socialsoftware.edition.core.dto;

public class FragmentDTO {
	private FragmentMetaInfoDTO meta;
	String text;

	public FragmentDTO() {
	}

	public FragmentMetaInfoDTO getMeta() {
		return this.meta;
	}

	public void setMeta(FragmentMetaInfoDTO meta) {
		this.meta = meta;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
