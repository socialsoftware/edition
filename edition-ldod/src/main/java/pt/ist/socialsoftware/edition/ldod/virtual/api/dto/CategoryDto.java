package pt.ist.socialsoftware.edition.ldod.virtual.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.user.api.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDto {

    private String externalId;
    private String acronym;
    private String urlId;
    private String name;
    private List<UserDto> users;

    public CategoryDto(Category category, VirtualEditionInter inter){
        setExternalId(category.getExternalId());
        setAcronym(category.getTaxonomy().getEdition().getAcronym());
        setUrlId(category.getUrlId());
        setName(category.getNameInEditionContext(inter.getEdition().getTaxonomy().getEdition()));
        setUsers(inter.getContributorSet().stream().map(UserDto::new).collect(Collectors.toList()));
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}
