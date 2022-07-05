package pt.ist.socialsoftware.edition.ldod.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Fragment;

public class MainFragmentDto {

    private String xmlId;

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public MainFragmentDto(Fragment fragment) {
        this.xmlId = fragment.getXmlId();
    }
}
