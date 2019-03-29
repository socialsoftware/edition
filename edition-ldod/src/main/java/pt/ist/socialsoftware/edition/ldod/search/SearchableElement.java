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

    public SearchableElement(Type type, String xmlId, String title, String fragmentXmlId) {
        this.type = type;
        this.xmlId = xmlId;
        this.title = title;
        this.fragmentXmlId = fragmentXmlId;
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

}
