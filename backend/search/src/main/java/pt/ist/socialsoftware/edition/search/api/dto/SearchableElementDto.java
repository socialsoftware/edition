package pt.ist.socialsoftware.edition.search.api.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import pt.ist.socialsoftware.edition.search.feature.SearchProcessor;

public class SearchableElementDto {
    public enum Type {
        SCHOLAR_INTER,
        VIRTUAL_INTER
    }

    private Type type;

    private String xmlId;

    private String title;

    private String fragmentXmlId;

    private String urlId;

    private String shortName;

    private String lastUsedId;

    public SearchableElementDto(Type type, String xmlId, String title, String fragmentXmlId, String urlId, String shortName, String lastUsedId) {
        this.type = type;
        this.xmlId = xmlId;
        this.title = title;
        this.fragmentXmlId = fragmentXmlId;
        this.urlId = urlId;
        this.shortName = shortName;
        this.lastUsedId = lastUsedId;
    }

    public SearchableElementDto() {}

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getXmlId() {
        return this.xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFragmentXmlId() {
        return this.fragmentXmlId;
    }

    public void setFragmentXmlId(String fragmentXmlId) {
        this.fragmentXmlId = fragmentXmlId;
    }

    public String getUrlId() {
        return this.urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLastUsedId() {
        return this.lastUsedId;
    }

    public void setLastUsedId(String lastUsedId) {
        this.lastUsedId = lastUsedId;
    }

    @Override
    public String toString() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
