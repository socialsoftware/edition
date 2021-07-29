package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

public class CategoryDto {
	private String acronym;
	private String urlId;
	private String name;
	private List<UserDto> sortedUsersList;
	private List<VirtualEditionListDto> sortedEditionsList;
	private List<FragInterDto> sortedIntersList;
	private String title;
	private int size;
	private String externalId;
	private String normalName;
	private VirtualTaxonomyDto taxonomyDto;
	
	public CategoryDto(Category category, String type) {
		if(type == "taxonomyPage" || type == "categoryPage") {
			this.setTitle(category.getTaxonomy().getEdition().getTitle());
			this.setAcronym(category.getTaxonomy().getEdition().getAcronym());
			this.setUrlId(category.getUrlId());
			this.setName(category.getName());
			this.setSize(category.getTagSet().size());
		}
		if(type == "taxonomyPage") {
			this.setSortedUsersList(category.getSortedUsers().stream()
					.map(UserDto::new)
					.collect(Collectors.toList()));
			this.setSortedEditionsList(category.getSortedEditions().stream()
								.map(vEdition -> new VirtualEditionListDto(vEdition, "shallow"))
								.collect(Collectors.toList()));
			this.setSortedIntersList(category.getSortedInters().stream()
								.map(FragInter -> new FragInterDto(FragInter, "shallow"))
								.collect(Collectors.toList()));
		}
		if(type == "categoryPage") {
			this.setSortedIntersList(category.getSortedInters().stream()
					.map(FragInter -> new FragInterDto(FragInter, category))
					.collect(Collectors.toList()));
		}
	}
	
	public CategoryDto(Category category) {
			this.setTitle(category.getTaxonomy().getEdition().getTitle());
			this.setAcronym(category.getTaxonomy().getEdition().getAcronym());
			this.setUrlId(category.getUrlId());
			this.setName(category.getName());
			this.setSize(category.getTagSet().size());
	}
	
	public CategoryDto(Category category, VirtualEdition virtualEdition, boolean bool) {
		this.setAcronym(category.getTaxonomy().getEdition().getAcronym());
		this.setUrlId(category.getUrlId());
		this.setName(category.getNameInEditionContext(virtualEdition));
		this.setExternalId(category.getExternalId());
		this.setNormalName(category.getName());
	}
	
	public CategoryDto(Category category, VirtualEdition virtualEdition) {
		this.setAcronym(category.getTaxonomy().getEdition().getAcronym());
		this.setUrlId(category.getUrlId());
		this.setName(category.getNameInEditionContext(virtualEdition));
		this.setSortedIntersList(category.getSortedInters(virtualEdition).stream()
				.map(FragInter -> new FragInterDto(FragInter, category))
				.collect(Collectors.toList()));
		this.setExternalId(category.getExternalId());
		this.setNormalName(category.getName());
	}
	
	
	public CategoryDto(Category category, LdoDUser user) {
		this.setTaxonomyDto(new VirtualTaxonomyDto(category.getTaxonomy().getEdition(), user));
		this.setTitle(category.getTaxonomy().getEdition().getTitle());
		this.setAcronym(category.getTaxonomy().getEdition().getAcronym());
		this.setUrlId(category.getUrlId());
		this.setName(category.getName());
		this.setExternalId(category.getExternalId());
		this.setNormalName(category.getName());
		this.setSortedIntersList(category.getSortedInters(category.getTaxonomy().getEdition()).stream()
				.map(FragInter -> new FragInterDto(FragInter, category))
				.collect(Collectors.toList()));

	}
	

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UserDto> getSortedUsersList() {
		return sortedUsersList;
	}

	public void setSortedUsersList(List<UserDto> sortedUsersList) {
		this.sortedUsersList = sortedUsersList;
	}

	public List<VirtualEditionListDto> getSortedEditionsList() {
		return sortedEditionsList;
	}

	public void setSortedEditionsList(List<VirtualEditionListDto> sortedEditionsList) {
		this.sortedEditionsList = sortedEditionsList;
	}

	public List<FragInterDto> getSortedIntersList() {
		return sortedIntersList;
	}

	public void setSortedIntersList(List<FragInterDto> sortedIntersList) {
		this.sortedIntersList = sortedIntersList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getNormalName() {
		return normalName;
	}

	public void setNormalName(String normalName) {
		this.normalName = normalName;
	}

	public VirtualTaxonomyDto getTaxonomyDto() {
		return taxonomyDto;
	}

	public void setTaxonomyDto(VirtualTaxonomyDto taxonomyDto) {
		this.taxonomyDto = taxonomyDto;
	}
}
