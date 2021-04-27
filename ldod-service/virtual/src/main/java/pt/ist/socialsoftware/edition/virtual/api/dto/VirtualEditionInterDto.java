package pt.ist.socialsoftware.edition.virtual.api.dto;


import pt.ist.socialsoftware.edition.virtual.api.textDto.FragmentDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.ScholarInterDto;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.utils.CategoryDTO;
import pt.ist.socialsoftware.edition.virtual.utils.FragInterDto;
import pt.ist.socialsoftware.edition.virtual.utils.RangeJson;

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
    private String reference;
    private int number;
    private ScholarInterDto lastUsed;

    /*public VirtualEditionInterDto(String xmlId) {
        setXmlId(xmlId);
        VirtualEditionInter virtualEditionInter = VirtualModule.getInstance().getVirtualEditionInterByXmlId(xmlId);

        this.externalId = virtualEditionInter.getExternalId();
        this.title = virtualEditionInter.getTitle();
        this.fragId = virtualEditionInter.getFragmentXmlId();
        this.urlId = virtualEditionInter.getUrlId();
        this.shortName = virtualEditionInter.getShortName();
        this.reference = virtualEditionInter.getReference();
        this.number = virtualEditionInter.getNumber();
    }*/

    public VirtualEditionInterDto(VirtualEditionInter virtualEditionInter) {
        setXmlId(virtualEditionInter.getXmlId());
        this.externalId = virtualEditionInter.getExternalId();
        this.title = virtualEditionInter.getTitle();
        this.fragId = virtualEditionInter.getFragmentXmlId();
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
        return this.fragId;
    }

    public String getUrlId() {
        //return this.virtualProvidesInterface.getVirtualEditionInterUrlId(this.xmlId);
        return this.urlId;
    }

    public String getShortName() {
        //return this.virtualProvidesInterface.getVirtualEditionInterShortName(this.xmlId);
        return this.shortName;
    }

    public ScholarInterDto getLastUsed() {
        //return this.virtualProvidesInterface.getVirtualEditionLastUsedScholarInter(this.xmlId);
        if (this.lastUsed == null)
            this.lastUsed = this.virtualProvidesInterface.getVirtualEditionLastUsedScholarInter(this.xmlId);
        return this.lastUsed;
    }

    public String getUsesScholarInterId() {
        return getLastUsed().getXmlId();
    }

    public String getReference() {
        //return this.virtualProvidesInterface.getVirtualEditionInterReference(this.xmlId);
        return this.reference;
    }

    public int getNumber() {
        //return this.virtualProvidesInterface.getVirtualEditionInterNumber(this.xmlId);
        return this.number;
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

    public List<String> getSortedCategoriesName() {
        return this.virtualProvidesInterface.getSortedVirtualEditionInterCategoriesName(this.xmlId);
    }

    public List<CategoryDto> getSortedCategories(VirtualEditionDto virtualEditionDto) {
        return this.virtualProvidesInterface.getSortedVirtualEditionInterCategoriesFromVirtualEdition(this.xmlId, virtualEditionDto.getAcronym());
    }

    public List<CategoryDto> getCategoriesUsedInTags(String username) {
        return this.virtualProvidesInterface.getCategoriesUsedInTags(this.xmlId, username);
    }

    public List<CategoryDTO> getAllDepthCategoriesAccessibleByUser(String username) {
        return this.virtualProvidesInterface.getVirtualEditionInterAllDepthCategoriesAccessibleByUser(this.xmlId, username);
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
        return this.virtualProvidesInterface.getVirtualEditionOfVirtualEditionInter(this.xmlId);
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

    public Set<String> getContributorSet(String externalId, String username) {
        return this.virtualProvidesInterface.getContributorSetFromVirtualEditionInter(externalId, this.xmlId, username);
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

    public void dissociate(String authenticatedUser, String category) {
        this.virtualProvidesInterface.dissociateVirtualEditionInterCategory(this.xmlId, authenticatedUser, category);
    }

    public void associate(String authenticatedUser, Set<String> collect) {
        this.virtualProvidesInterface.associateVirtualEditionInterCategoriesbyExternalId(this.externalId, authenticatedUser, collect);
    }

    public HumanAnnotationDto createHumanAnnotation(String quote, String text, String username, List<RangeJson> ranges, List<String> tags) {
       return this.virtualProvidesInterface.createHumanAnnotation(this.xmlId, quote, text, username, ranges, tags);
    }
}
