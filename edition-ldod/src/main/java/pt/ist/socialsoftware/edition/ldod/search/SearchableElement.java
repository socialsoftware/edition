package pt.ist.socialsoftware.edition.ldod.search;

public class SearchableElement {
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

    public SearchableElement(Type type, String xmlId, String title, String fragmentXmlId, String urlId, String shortName, String lastUsedId) {
        this.type = type;
        this.xmlId = xmlId;
        this.title = title;
        this.fragmentXmlId = fragmentXmlId;
        this.urlId = urlId;
        this.shortName = shortName;
        this.lastUsedId = lastUsedId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFragmentXmlId() {
        return fragmentXmlId;
    }

    public void setFragmentXmlId(String fragmentXmlId) {
        this.fragmentXmlId = fragmentXmlId;
    }

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLastUsedId() {
        return lastUsedId;
    }

    public void setLastUsedId(String lastUsedId) {
        this.lastUsedId = lastUsedId;
    }
}
