package pt.ist.socialsoftware.edition.ldod.virtual.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;


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

    public String getUsesScholarInterId() {
        return this.virtualProvidesInterface.getUsesVirtualEditionInterId(this.xmlId);
    }

    public String getExternalId(){
        return this.virtualProvidesInterface.getVirtualEditionInterExternalId(this.xmlId);
    }

    public int getNumber(){
        return this.virtualProvidesInterface.getVirtualEditionInterNumber(this.xmlId);
    }

    public VirtualEditionInterDto getNextInter(){
        return this.virtualProvidesInterface.getNextVirtualInter(this.xmlId);
    }

    public VirtualEditionInterDto getPrevInter(){
        return this.virtualProvidesInterface.getPrevVirtualInter(this.xmlId);
    }
}
