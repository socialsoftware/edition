package pt.ist.socialsoftware.edition.text.api.dto;

import pt.ist.socialsoftware.edition.text.domain.SimpleText;

public class SimpleTextDto {

    private String xmlId;
    private String externalId;

    public SimpleTextDto(SimpleText simpleText) {
        this.xmlId = simpleText.getXmlId();
        this.externalId = simpleText.getExternalId();
    }

    public String getXmlId() {
        return xmlId;
    }

    public String getExternalId() {
        return externalId;
    }
}
