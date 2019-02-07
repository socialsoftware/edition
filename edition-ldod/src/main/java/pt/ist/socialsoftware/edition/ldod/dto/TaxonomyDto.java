package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.Taxonomy;

public class TaxonomyDto {
	private boolean openManagement;
	private boolean openVocabulary;
	private boolean openAnnotation;
	private List<String> categories;

	public TaxonomyDto() {
	}

	public TaxonomyDto(Taxonomy taxonomy) {
		this.setOpenManagement(taxonomy.getOpenManagement());
		this.setOpenVocabulary(taxonomy.getOpenVocabulary());
		this.setOpenAnnotation(taxonomy.getOpenAnnotation());
		this.categories = taxonomy.getCategoriesSet().stream().map(c -> c.getName()).sorted()
				.collect(Collectors.toList());
	}

	public boolean isOpenManagement() {
		return this.openManagement;
	}

	public void setOpenManagement(boolean openManagement) {
		this.openManagement = openManagement;
	}

	public boolean isOpenVocabulary() {
		return this.openVocabulary;
	}

	public void setOpenVocabulary(boolean openVocabulary) {
		this.openVocabulary = openVocabulary;
	}

	public boolean isOpenAnnotation() {
		return this.openAnnotation;
	}

	public void setOpenAnnotation(boolean openAnnotation) {
		this.openAnnotation = openAnnotation;
	}

	public List<String> getCategories() {
		return this.categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

}
