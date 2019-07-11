package pt.ist.socialsoftware.edition.ldod.text.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Heteronym;

public class HeteronymDto {
    private String name;

    private String xmlId;

    public HeteronymDto(Heteronym heteronym) {
        setName(heteronym.getName());
        setXmlId(heteronym.getXmlId());
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

}
