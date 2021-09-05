package pt.ist.socialsoftware.edition.notification.dtos.text;



public class HeteronymDto {

    public static String NULL_NAME = "não atribuído";

    private String name;

    private String xmlId;

    private String externalId;


    public HeteronymDto() {
        super();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXmlId() {
        return this.xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public boolean isNullHeteronym() {
        return this.name.equals(NULL_NAME);
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
