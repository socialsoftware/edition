package pt.ist.socialsoftware.edition.ldod.dto;

public class FragmentDto {
	private FragmentMetaInfoDto meta;
	String text;

	public FragmentDto() {
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
