package pt.ist.socialsoftware.edition.ldod.frontend.utils.dto;


import pt.ist.socialsoftware.edition.virtual.api.textdto.FragmentDto;

public class MainFragmentDto {

    private String xmlId;

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public MainFragmentDto(FragmentDto fragment) {
        this.xmlId = fragment.getXmlId();
    }
}
