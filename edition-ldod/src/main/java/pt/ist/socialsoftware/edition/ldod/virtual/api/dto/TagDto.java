package pt.ist.socialsoftware.edition.ldod.virtual.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Tag;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class TagDto {

    private String username;
    private String acronym;
    private String urlId;
    private String name;

    public TagDto(Tag tag, VirtualEditionInter inter){
        setUsername(tag.getContributor());
        setAcronym(tag.getCategory().getTaxonomy().getEdition().getAcronym());
        setUrlId(tag.getCategory().getUrlId());
        setName(tag.getCategory().getNameInEditionContext(inter.getEdition()));
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
