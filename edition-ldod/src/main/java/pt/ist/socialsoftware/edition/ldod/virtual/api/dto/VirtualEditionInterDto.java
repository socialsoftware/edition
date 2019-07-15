package pt.ist.socialsoftware.edition.ldod.virtual.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;

import java.util.List;


public class VirtualEditionInterDto {
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    private String xmlId;

    public VirtualEditionInterDto(String xmlId) {
        setXmlId(xmlId);
    }

    public VirtualEditionInterDto(VirtualEditionInter virtualEditionInter) {
        setXmlId(virtualEditionInter.getXmlId());
    }

    public String getXmlId() {
        return this.xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getExternalId() {
        return this.virtualProvidesInterface.getVirtualEditionInterExternalId(this.xmlId);
    }

    public String getTitle() {
        return this.virtualProvidesInterface.getVirtualEditionInterTitle(this.xmlId);
    }

    public String getFragmentXmlId() {
        return this.virtualProvidesInterface.getFragmentXmlIdVirtualEditionInter(this.xmlId);
    }

    public String getUrlId() {
        return this.virtualProvidesInterface.getVirtualEditionInterUrlId(this.xmlId);
    }

    public String getShortName() {
        return this.virtualProvidesInterface.getVirtualEditionInterShortName(this.xmlId);
    }

    public ScholarInterDto getLastUsed() {
        return this.virtualProvidesInterface.getVirtualEditionLastUsedScholarInter(this.xmlId);

    }

    public List<String> getCategorySet() {
        return this.virtualProvidesInterface.getVirtualEditionInterCategoryList(this.xmlId);
    }

}
