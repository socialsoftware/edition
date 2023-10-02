package pt.ist.socialsoftware.edition.notification.dtos.virtual;

import java.io.Serializable;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String text;
	private String selected;

	public CategoryDto() {
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
