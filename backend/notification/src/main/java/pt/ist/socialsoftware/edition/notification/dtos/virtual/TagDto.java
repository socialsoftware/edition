package pt.ist.socialsoftware.edition.notification.dtos.virtual;


public class TagDto {

    private String username;
    private String acronym;
    private String urlId;
    private String nameInEdition;
    private String name;
    private String externalId;

    public TagDto() {}

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
