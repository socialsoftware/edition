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

    public String getUsesScholarInterId() {
        return getLastUsed().getXmlId();
    }

    public List<String> getCategorySet() {
        return this.virtualProvidesInterface.getVirtualEditionInterCategoryList(this.xmlId);
    }

    public String getReference(){
        return this.virtualProvidesInterface.getVirtualEditionInterReference(this.xmlId);
    }

    public int getNumber() {
        return this.virtualProvidesInterface.getVirtualEditionInterNumber(this.xmlId);
    }

    public VirtualEditionInterDto getNextInter() {
        return this.virtualProvidesInterface.getNextVirtualInter(this.xmlId);
    }

    public VirtualEditionInterDto getPrevInter() {
        return this.virtualProvidesInterface.getPrevVirtualInter(this.xmlId);
    }

    public VirtualEditionInterDto getUsesInter(){
        return this.virtualProvidesInterface.getVirtualEditionInterUses(this.xmlId);
    }

    public List<CategoryDto> getAssignedCategories(){
        return this.virtualProvidesInterface.getVirtualEditionInterAssignedCategories(this.xmlId);
    }

    public List<CategoryDto> getAssignedCategoriesForUser(String username){
        return this.virtualProvidesInterface.getVirtualEditionInterAssignedCategoriesForUser(this.xmlId, username);
    }

    public List<CategoryDto> getNonAssignedCategoriesForUser(String username){
        return this.virtualProvidesInterface.getVirtualEditionInterNonAssignedCategoriesForUser(this.xmlId, username);
    }
}
