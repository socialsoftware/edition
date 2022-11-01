package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.Tag;

public class TagDto {

    private String username;
    private String acronym;
    private String urlId;
    private String name;

    public TagDto(Tag tag) {
        setUsername(tag.getContributor().getUsername());
        setAcronym(tag.getCategory().getTaxonomy().getEdition().getAcronym());
        setUrlId(tag.getCategory().getUrlId());
        setName(tag.getCategory().getNameInEditionContext(tag.getInter().getVirtualEdition()));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
