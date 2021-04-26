package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.Taxonomy;

public class TaxonomyMicrofrontendDto {
	private String title;
	private String acronym;
	private int categorySetSize;
	private List<CategoryDto> categorySet;

	public TaxonomyMicrofrontendDto(Taxonomy taxonomy) {
		this.setTitle(taxonomy.getEdition().getTitle());
		this.setAcronym(taxonomy.getEdition().getAcronym());
		this.setCategorySetSize(taxonomy.getCategoriesSet().size());
		this.setCategorySet(taxonomy.getCategoriesSet().stream()
									.map(CategoryDto::new)
									.collect(Collectors.toList()));
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public int getCategorySetSize() {
		return categorySetSize;
	}

	public void setCategorySetSize(int categorySetSize) {
		this.categorySetSize = categorySetSize;
	}

	public List<CategoryDto> getCategorySet() {
		return categorySet;
	}

	public void setCategorySet(List<CategoryDto> categorySet) {
		this.categorySet = categorySet;
	}
}
