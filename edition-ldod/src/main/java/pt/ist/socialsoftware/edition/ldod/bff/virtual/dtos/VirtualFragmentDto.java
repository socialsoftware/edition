package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.Fragment;

public class VirtualFragmentDto {

    private String externalId;
    private String xmlId;
    private String title;

    public VirtualFragmentDto(Fragment fragment) {
        setExternalId(fragment.getExternalId());
        setTitle(fragment.getTitle());
        setXmlId(fragment.getXmlId());
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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
}
