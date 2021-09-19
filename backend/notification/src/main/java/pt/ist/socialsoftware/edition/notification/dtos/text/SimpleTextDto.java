package pt.ist.socialsoftware.edition.notification.dtos.text;


public class SimpleTextDto {

    private String xmlId;
    private String externalId;


    public SimpleTextDto() {
        super();
    }

    public String getXmlId() {
        return xmlId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
