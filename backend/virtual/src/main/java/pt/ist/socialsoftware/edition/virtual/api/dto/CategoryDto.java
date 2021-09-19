package pt.ist.socialsoftware.edition.virtual.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pt.ist.socialsoftware.edition.notification.dtos.user.UserDto;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.domain.Category;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEditionInter;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDto {

    private String externalId;
    private String acronym;
    private String urlId;
    private String nameInEdition;
    private String name;
    private List<UserDto> users;
    private boolean hasTags;

    public CategoryDto(Category category, VirtualEditionInter inter, String username) {
        setExternalId(category.getExternalId());
        setAcronym(category.getTaxonomy().getEdition().getAcronym());
        setUrlId(category.getUrlId());
        setNameInEdition(category.getNameInEditionContext(inter.getEdition().getTaxonomy().getEdition()));
        setName(category.getName());
        setUsers(inter.getContributorSet(category, username).stream().map(UserDto::new).collect(Collectors.toList()));
        this.hasTags = category.getTagSet().isEmpty();
    }

    public CategoryDto(Category category) {
        setExternalId(category.getExternalId());
        setAcronym(category.getTaxonomy().getEdition().getAcronym());
        setUrlId(category.getUrlId());
//        setNameInEdition(category.getNameInEditionContext(inter.getEdition().getTaxonomy().getEdition()));
        setName(category.getName());
        this.hasTags = category.getTagSet().isEmpty();
    }

    public CategoryDto() {}

    public String getExternalId() {
        return this.externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getUrlId() {
        return this.urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getNameInEdition() {
        return this.nameInEdition;
    }

    public void setNameInEdition(String name) {
        this.nameInEdition = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserDto> getUsers() {
        return this.users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }

    @JsonIgnore
    public List<String> getUsernames() {
        return getUsers().stream().map(userDto -> userDto.getUsername()).collect(Collectors.toList());
    }

    public boolean isHasTags() { return this.hasTags; }

}
