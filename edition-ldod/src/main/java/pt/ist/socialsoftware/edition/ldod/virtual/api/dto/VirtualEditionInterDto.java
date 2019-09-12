package pt.ist.socialsoftware.edition.ldod.virtual.api.dto;

import pt.ist.socialsoftware.edition.ldod.api.ui.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class VirtualEditionInterDto {
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    private String xmlId;

    // cached attributes
    private String externalId;
    private String title;
    private String fragId;
    private String urlId;
    private String shortName;
    private ScholarInterDto lastUsed;
    private String reference;
    private VirtualEditionDto editionDto;

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
        if (externalId == null)
            externalId = this.virtualProvidesInterface.getVirtualEditionInterExternalId(this.xmlId);
        return externalId;
    }

    public String getTitle() {
        if (title == null)
            title = this.virtualProvidesInterface.getVirtualEditionInterTitle(this.xmlId);
        return title;
    }

    public FragInterDto.InterType getType() {
        return FragInterDto.InterType.VIRTUAL;
    }

    public String getFragmentXmlId() {
        if (fragId == null)
            fragId = this.virtualProvidesInterface.getFragmentXmlIdVirtualEditionInter(this.xmlId);
        return fragId;
    }

    public String getUrlId() {
        if (urlId == null)
            urlId = this.virtualProvidesInterface.getVirtualEditionInterUrlId(this.xmlId);
        return urlId;
    }

    public String getShortName() {
        if (shortName == null)
            shortName = this.virtualProvidesInterface.getVirtualEditionInterShortName(this.xmlId);
        return shortName;
    }

    public ScholarInterDto getLastUsed() {
        if (lastUsed == null)
            lastUsed = this.virtualProvidesInterface.getVirtualEditionLastUsedScholarInter(this.xmlId);
        return lastUsed;
    }

    public String getUsesScholarInterId() {
        return getLastUsed().getXmlId();
    }

    public String getReference() {
        if (reference == null)
            reference = this.virtualProvidesInterface.getVirtualEditionInterReference(this.xmlId);
        return reference;
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

    public Set<TagDto> getAllDepthTagsAccessibleByUser(String username) {
        return this.virtualProvidesInterface.getVirtualEditionInterAllDepthTagsAccessibleByUser(this.xmlId, username);
    }

    public Set<TagDto> getTagSet(){
        return this.virtualProvidesInterface.getAllTags(xmlId);
    }

    public List<CategoryDto> getAllDepthCategoriesUsedByUserInTags(String username) {
        return this.virtualProvidesInterface.getVirtualEditionInterAllDepthCategoriesUsedByUserInTags(this.xmlId, username);
    }

    public List<CategoryDto> getAllDepthCategoriesNotUsedInTags(String username) {
        return this.virtualProvidesInterface.getVirtualEditionInterAllDepthCategoriesNotUsedInTags(this.xmlId, username);
    }

    public VirtualEditionDto getVirtualEditionDto() {
        if (editionDto == null)
            editionDto = this.virtualProvidesInterface.getVirtualEditionOfVirtualEditionInter(this.xmlId);
        return editionDto;
    }

    public List<AnnotationDto> getAllDepthAnnotationsAccessibleByUser(String username) {
        return this.virtualProvidesInterface.getAllDepthAnnotationsAccessibleByUser(this.xmlId, username);
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

    public List<CategoryDto> getAllDepthCategoriesUsedInTags(String username) {
        return this.virtualProvidesInterface.getVirtualEditionInterAllDepthCategoriesUsedInTags(this.xmlId, username);
    }

    public FragmentDto getFragmentDto() {
        return getLastUsed().getFragmentDto();
    }

    public String getAllDepthCategoriesJSON(String username) {
        return this.virtualProvidesInterface.getAllDepthCategoriesJSON(this.xmlId, username);
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
