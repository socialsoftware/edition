package pt.ist.socialsoftware.edition.ldod.bff.search.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Heteronym;

public class AdvancedHeteronymDto {
    private String name;
    private String xmlId;

    public AdvancedHeteronymDto(Heteronym heteronym) {
        setName(heteronym.getName());
        setXmlId(heteronym.getXmlId());
    }
    public AdvancedHeteronymDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    @Override
    public String toString() {
        return "AdvancedHeteronymDto{" +
                "name='" + name + '\'' +
                ", xmlId='" + xmlId + '\'' +
                '}';
    }
}
