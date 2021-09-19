package pt.ist.socialsoftware.edition.virtual.api.dto;

import pt.ist.socialsoftware.edition.virtual.domain.Tag;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEditionInter;

public class TagDto {

    private String username;
    private String acronym;
    private String urlId;
    private String nameInEdition;
    private String name;
    private String externalId;

    public TagDto(Tag tag, VirtualEditionInter inter){
        setUsername(tag.getContributor());
        setAcronym(tag.getCategory().getTaxonomy().getEdition().getAcronym());
        setUrlId(tag.getCategory().getUrlId());
        setNameInEdition(tag.getCategory().getNameInEditionContext(inter.getEdition()));
        setName(tag.getCategory().getName());
        setExternalId(tag.getExternalId());
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

    public String getNameInEdition() {
        return nameInEdition;
    }

    public void setNameInEdition(String nameInEdition) {
        this.nameInEdition = nameInEdition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
