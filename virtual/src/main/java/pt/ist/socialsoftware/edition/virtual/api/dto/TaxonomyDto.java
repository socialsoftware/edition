package pt.ist.socialsoftware.edition.virtual.api.dto;

import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.domain.Category;
import pt.ist.socialsoftware.edition.virtual.domain.Taxonomy;
import pt.ist.socialsoftware.edition.virtual.utils.TopicListDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaxonomyDto {

	private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();


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

	public VirtualEditionDto getEdition() {
		return this.virtualProvidesInterface.getVirtualEdition(this.editionAcronym);
	}

	public Set<CategoryDto> getCategoriesSet() {
		return this.virtualProvidesInterface.getCategoriesFromTaxonomy(editionAcronym);
	}

	public void edit(boolean management, boolean vocabulary, boolean annotation) {
		this.virtualProvidesInterface.editTaxonomy(this.editionAcronym, management, vocabulary, annotation);
	}

	public void createGeneratedCategories(TopicListDTO topicList) {
		this.virtualProvidesInterface.createGeneratedCategories(this.editionAcronym, topicList);
	}

	public void removeTaxonomy() {
		this.virtualProvidesInterface.removeTaxonomy(this.editionAcronym);
	}

	public void createCategory(String name) {
		this.virtualProvidesInterface.createCategory(this.editionAcronym, name);
	}

	public CategoryDto mergeCategories(List<CategoryDto> categories) {
		return this.virtualProvidesInterface.mergeCategories(this.editionAcronym, categories);
	}

	public void deleteCategories(List<CategoryDto> categories) {
		this.virtualProvidesInterface.deleteCategories(this.editionAcronym, categories);
	}

	public CategoryDto extractCategory(String externalId, String[] interIds) {
		return this.virtualProvidesInterface.extractCategories(this.editionAcronym, externalId, interIds);
	}

	public List<VirtualEditionDto> getUsedIn() {
		return this.virtualProvidesInterface.getTaxonomyUsedIn(this.editionAcronym);
	}

	public boolean canManipulateTaxonomy(String username) {
		return this.virtualProvidesInterface.canManipulateTaxonomy(this.editionAcronym, username);
	}

	public List<CategoryDto> getSortedCategories() {
		return getCategoriesSet().stream().sorted((c1, c2) -> c1.getName().compareTo(c2.getName()))
				.collect(Collectors.toList());
	}
}
