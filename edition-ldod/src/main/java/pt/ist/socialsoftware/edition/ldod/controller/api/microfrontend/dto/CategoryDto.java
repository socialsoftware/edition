package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<UserShortDto> sortedUserShortList;
    private List<String> sortedTitleList;
    private List<FragInterShortDto> sortedIntersShortList;

    public CategoryDto(String name) {
        setName(name);
    }


    public CategoryDto(Category category, PresentationTarget type) {

            setTitle(category.getTaxonomy().getEdition().getTitle());
            setAcronym(category.getTaxonomy().getEdition().getAcronym());
            setUrlId(category.getUrlId());
            setName(category.getName());
            setSize(category.getTagSet().size());
        if (type.equals(PresentationTarget.TAXONOMY_PAGE)) {
            setSortedUserShortList(category.getSortedUsers().stream()
                    .map(UserShortDto::new)
                    .collect(Collectors.toList()));
            setSortedTitleList(category.getSortedEditions().stream()
                    .map(vEdition -> new String(vEdition.getTitle()))
                    .collect(Collectors.toList()));

            setSortedIntersShortList(category.getSortedInters().stream()
                    .map(FragInterShortDto::new)
                    .collect(Collectors.toList()));
        }
        if (type.equals(PresentationTarget.CATEGORY_PAGE)) {
            setSortedIntersList(category.getSortedInters().stream()
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

    public CategoryDto(Category category, VirtualEdition virtualEdition, String type) {
        if (type == "shallow") {
            this.setAcronym(category.getTaxonomy().getEdition().getAcronym());
            this.setUrlId(category.getUrlId());
            this.setName(category.getNameInEditionContext(virtualEdition));
        } else {
            this.setAcronym(category.getTaxonomy().getEdition().getAcronym());
            this.setUrlId(category.getUrlId());
            this.setName(category.getNameInEditionContext(virtualEdition));
            this.setSortedIntersShortList(category.getSortedInters().stream()
                    .map(FragInter -> new FragInterShortDto(FragInter))
                    .collect(Collectors.toList()));
            this.setExternalId(category.getExternalId());
            this.setNormalName(category.getName());
        }

    }

    public CategoryDto(Category category, LdoDUser user) {
        this.setTaxonomyDto(new VirtualTaxonomyDto(category.getTaxonomy().getEdition(), user));
        this.setTitle(category.getTaxonomy().getEdition().getTitle());
        this.setAcronym(category.getTaxonomy().getEdition().getAcronym());
        this.setUrlId(category.getUrlId());
        this.setName(category.getName());
        this.setExternalId(category.getExternalId());
        this.setNormalName(category.getName());
        this.setSortedIntersShortList(category.getSortedInters().stream()
                .map(FragInter -> new FragInterShortDto(FragInter))
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

    public List<UserShortDto> getSortedUserShortList() {
        return sortedUserShortList;
    }

    public void setSortedUserShortList(List<UserShortDto> sortedUserShortList) {
        this.sortedUserShortList = sortedUserShortList;
    }

    public List<String> getSortedTitleList() {
        return sortedTitleList;
    }

    public void setSortedTitleList(List<String> sortedTitleList) {
        this.sortedTitleList = sortedTitleList;
    }

    public List<FragInterShortDto> getSortedIntersShortList() {
        return sortedIntersShortList;
    }

    public void setSortedIntersShortList(List<FragInterShortDto> sortedIntersShortList) {
        this.sortedIntersShortList = sortedIntersShortList;
    }

    public enum PresentationTarget {
        TAXONOMY_PAGE, CATEGORY_PAGE, MANAGE_VIRTUAL
    }
}
