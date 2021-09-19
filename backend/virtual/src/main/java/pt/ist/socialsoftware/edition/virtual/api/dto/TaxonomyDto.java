package pt.ist.socialsoftware.edition.virtual.api.dto;

import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.domain.Category;
import pt.ist.socialsoftware.edition.virtual.domain.Taxonomy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaxonomyDto {

	private boolean openManagement;
	private boolean openVocabulary;
	private boolean openAnnotation;
	private boolean hasCategories;

	private String editionAcronym;
	private String externalId;

	public TaxonomyDto() {
	}

	public TaxonomyDto(Taxonomy taxonomy) {
		this.setOpenManagement(taxonomy.getOpenManagement());
		this.setOpenVocabulary(taxonomy.getOpenVocabulary());
		this.setOpenAnnotation(taxonomy.getOpenAnnotation());
		this.setHasCategories(!taxonomy.getCategoriesSet().isEmpty());
		this.editionAcronym = taxonomy.getEdition().getAcronym();
		this.externalId = taxonomy.getExternalId();
	}

	public String getExternalId() {
		return externalId;
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

	public boolean getHasCategories() {
		return this.hasCategories;
	}

	public void setHasCategories(boolean hasCategories) {
		this.hasCategories = hasCategories;
	}

	public String getEditionAcronym() {
		return editionAcronym;
	}

	public void setEditionAcronym(String editionAcronym) {
		this.editionAcronym = editionAcronym;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
}
