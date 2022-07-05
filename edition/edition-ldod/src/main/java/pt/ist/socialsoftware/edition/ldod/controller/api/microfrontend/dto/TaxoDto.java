package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.Taxonomy;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

public class TaxoDto {
	private String title;
	private String acronym;
	private int categorySetSize;
	private List<CategoryDto> categorySet;
	private List<VirtualEditionDto> usedInList;
	private String externalId;
	private boolean canManipulate;

	public TaxoDto(Taxonomy taxonomy) {
		this.setTitle(taxonomy.getEdition().getTitle());
		this.setAcronym(taxonomy.getEdition().getAcronym());
		this.setCategorySetSize(taxonomy.getCategoriesSet().size());
		this.setCategorySet(taxonomy.getCategoriesSet().stream()
									.map(category -> new CategoryDto(category, CategoryDto.PresentationTarget.TAXONOMY_PAGE))
									.collect(Collectors.toList()));
	}

	public TaxoDto(VirtualEdition vEdition, LdoDUser user) {
		this.setUsedInList(vEdition.getTaxonomy().getUsedIn().stream().map(VirtualEditionDto::new).collect(Collectors.toList()));
		this.setCanManipulate(vEdition.getTaxonomy().canManipulateTaxonomy(user));
		this.setExternalId(vEdition.getTaxonomy().getExternalId());
		this.setCategorySet(vEdition.getTaxonomy().getSortedCategories().stream()
				.map(category -> new CategoryDto(category, vEdition, "deep"))
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

	public List<VirtualEditionDto> getUsedInList() {
		return usedInList;
	}

	public void setUsedInList(List<VirtualEditionDto> usedInList) {
		this.usedInList = usedInList;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public boolean isCanManipulate() {
		return canManipulate;
	}

	public void setCanManipulate(boolean canManipulate) {
		this.canManipulate = canManipulate;
	}
}
