package pt.ist.socialsoftware.edition.text.api.dto;

import pt.ist.socialsoftware.edition.text.domain.Heteronym;
import pt.ist.socialsoftware.edition.text.domain.NullHeteronym;

public class HeteronymDto {
    private String name;

    private String xmlId;

    private String externalId;

    public HeteronymDto(Heteronym heteronym) {
        setName(heteronym.getName());
        setXmlId(heteronym.getXmlId());
        setExternalId(heteronym.getExternalId());
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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public boolean isNullHeteronym() {
        return this.name.equals(NullHeteronym.NULL_NAME);
    }
}
