package pt.ist.socialsoftware.edition.ldod.virtual.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.user.api.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDto {

    private String externalId;
    private String acronym;
    private String urlId;
    private String nameInEdition;
    private String name;
    private List<UserDto> users;

    public CategoryDto(Category category, VirtualEditionInter inter) {
        setExternalId(category.getExternalId());
        setAcronym(category.getTaxonomy().getEdition().getAcronym());
        setUrlId(category.getUrlId());
        setNameInEdition(category.getNameInEditionContext(inter.getEdition().getTaxonomy().getEdition()));
        setName(category.getName());
        setUsers(inter.getContributorSet(category).stream().map(UserDto::new).collect(Collectors.toList()));
    }

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
}
