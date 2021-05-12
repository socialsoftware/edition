package pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto;

import java.io.Serializable;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String text;
	private String selected;

	public CategoryDTO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String name) {
		this.text = name;
	}

	public String getSelected() {
		return this.selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

}
