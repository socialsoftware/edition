package pt.ist.socialsoftware.edition.ldod.virtual.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;

import java.util.ArrayList;
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

    public String getReference() {
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

    public VirtualEditionInterDto getUsesInter() {
        return this.virtualProvidesInterface.getVirtualEditionInterUses(this.xmlId);
    }

    public List<String> getSortedCategories() {
        return this.virtualProvidesInterface.getSortedVirtualEditionInterCategories(this.xmlId);
    }

    public List<CategoryDto> getCategoriesUsedInTags(String username) {
        return this.virtualProvidesInterface.getCategoriesUsedInTags(this.xmlId, username);
    }

    public List<CategoryDto> getAllDepthCategoriesUsedByUserInTags(String username) {
        return this.virtualProvidesInterface.getVirtualEditionInterAllDepthCategoriesUsedByUserInTags(this.xmlId, username);
    }

    public List<CategoryDto> getAllDepthCategoriesNotUsedInTags(String username) {
        return this.virtualProvidesInterface.getVirtualEditionInterAllDepthCategoriesNotUsedInTags(this.xmlId, username);
    }

    public VirtualEditionDto getVirtualEditionDto() {
        return this.virtualProvidesInterface.getVirtualEditionOfVirtualEditionInter(this.xmlId);
    }

    public List<TagDto> getAllDepthTagsNotHumanAnnotationAccessibleByUser(String username) {
        return this.virtualProvidesInterface.getAllDepthTagsNotHumanAnnotationAccessibleByUser(this.xmlId, username);
    }

    public List<HumanAnnotationDto> getVirtualEditionInterHumanAnnotationsAccessibleByUser(String username) {
        return this.virtualProvidesInterface.getVirtualEditionInterHumanAnnotationsAccessibleByUser(this.xmlId, username);
    }

    public List<AwareAnnotationDto> getVirtualEditionInterAwareAnnotationsAccessibleByUser(String username) {
        return this.virtualProvidesInterface.getVirtualEditionInterAwareAnnotationsAccessibleByUser(this.xmlId, username);
    }

    public List<VirtualEditionInterDto> getUsesPath() {
        List<VirtualEditionInterDto> usesPath = new ArrayList<>();
        VirtualEditionInterDto uses = getUsesInter();
        while (uses != null) {
            usesPath.add(uses);
            uses = uses.getUsesInter();
        }

        return usesPath;
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
