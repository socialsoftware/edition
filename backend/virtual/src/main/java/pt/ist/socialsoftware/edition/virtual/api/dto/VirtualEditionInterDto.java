package pt.ist.socialsoftware.edition.virtual.api.dto;


import pt.ist.socialsoftware.edition.virtual.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.utils.CategoryDTO;
import pt.ist.socialsoftware.edition.virtual.utils.FragInterDto;
import pt.ist.socialsoftware.edition.virtual.utils.RangeJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class VirtualEditionInterDto {

    private String xmlId;

    // cached attributes
    private String externalId;
    private String title;
    private String fragmentXmlId;
    private String urlId;
    private String shortName;
    private String reference;
    private int number;

    /*public VirtualEditionInterDto(String xmlId) {
        setXmlId(xmlId);
        VirtualEditionInter virtualEditionInter = VirtualModule.getInstance().getVirtualEditionInterByXmlId(xmlId);

        this.externalId = virtualEditionInter.getExternalId();
        this.title = virtualEditionInter.getTitle();
        this.fragmentXmlId = virtualEditionInter.getFragmentXmlId();
        this.urlId = virtualEditionInter.getUrlId();
        this.shortName = virtualEditionInter.getShortName();
        this.reference = virtualEditionInter.getReference();
        this.number = virtualEditionInter.getNumber();
    }*/

    public VirtualEditionInterDto(VirtualEditionInter virtualEditionInter) {
        setXmlId(virtualEditionInter.getXmlId());
        this.externalId = virtualEditionInter.getExternalId();
        this.title = virtualEditionInter.getTitle();
        this.fragmentXmlId = virtualEditionInter.getFragmentXmlId();
        this.urlId = virtualEditionInter.getUrlId();
        this.shortName = virtualEditionInter.getShortName();
        this.reference = virtualEditionInter.getReference();
        this.number = virtualEditionInter.getNumber();
    }

    public String getXmlId() {
        return this.xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getExternalId() {
        //return this.virtualProvidesInterface.getVirtualEditionInterExternalId(this.xmlId);
        return this.externalId;
    }

    public String getTitle() {
        //return this.virtualProvidesInterface.getVirtualEditionInterTitle(this.xmlId);
        return this.title;
    }

    public FragInterDto.InterType getType() {
        return FragInterDto.InterType.VIRTUAL;
    }

    public String getFragmentXmlId() {
        //return this.virtualProvidesInterface.getFragmentXmlIdVirtualEditionInter(this.xmlId);
        return this.fragmentXmlId;
    }

    public String getUrlId() {
        //return this.virtualProvidesInterface.getVirtualEditionInterUrlId(this.xmlId);
        return this.urlId;
    }

    public String getShortName() {
        //return this.virtualProvidesInterface.getVirtualEditionInterShortName(this.xmlId);
        return this.shortName;
    }


    public String getReference() {
        //return this.virtualProvidesInterface.getVirtualEditionInterReference(this.xmlId);
        return this.reference;
    }

    public int getNumber() {
        //return this.virtualProvidesInterface.getVirtualEditionInterNumber(this.xmlId);
        return this.number;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VirtualEditionInterDto other = (VirtualEditionInterDto) o;
        return this.xmlId.equals(other.getXmlId());
    }

    @Override
    public int hashCode() {
        return this.xmlId.hashCode();
    }

}
