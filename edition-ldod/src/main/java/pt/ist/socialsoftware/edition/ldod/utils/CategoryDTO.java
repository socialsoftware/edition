package pt.ist.socialsoftware.edition.ldod.utils;

import java.io.Serializable;

import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String text;
	private String selected;

	public CategoryDTO() {
	}

	public CategoryDTO(VirtualEdition virtualEdition, Category category) {
		this.id = category.getNameInEditionContext(virtualEdition);
		this.text = category.getNameInEditionContext(virtualEdition);
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
