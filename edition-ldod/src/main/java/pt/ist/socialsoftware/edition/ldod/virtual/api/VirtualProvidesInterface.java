package pt.ist.socialsoftware.edition.ldod.virtual.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.dto.WeightsDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.*;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionInterListDto;

import java.util.*;
import java.util.stream.Collectors;

public class VirtualProvidesInterface {
    private static final Logger logger = LoggerFactory.getLogger(VirtualProvidesInterface.class);

    public boolean isInterInVirtualEdition(String xmlId, String acronym) {
        return VirtualModule.getInstance().getVirtualEdition(acronym).getAllDepthVirtualEditionInters().stream()
                .anyMatch(virtualEditionInter -> virtualEditionInter.getXmlId().equals(xmlId));
    }

    public VirtualEditionDto getVirtualEdition(String acronym) {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (virtualEdition != null) {
            return new VirtualEditionDto(virtualEdition);
        }

        return null;
    }


    public Set<VirtualEditionDto> getPublicVirtualEditionSet() {
        return VirtualModule.getInstance().getVirtualEditionsSet().stream().filter(virtualEdition -> virtualEdition.getPub()).map(VirtualEditionDto::new).collect(Collectors.toSet());
    }

    public Set<VirtualEditionDto> getVirtualEditions() {
        return VirtualModule.getInstance().getVirtualEditionsSet().stream().map(VirtualEditionDto::new).collect(Collectors.toSet());
    }

    public String getVirtualEditionReference(String acronym) {
        return getVirtualEditionByAcronym(acronym).map(VirtualEdition::getReference).orElse(null);
    }

    public boolean isVirtualEditionPublicOrIsUserParticipant(String acronym){
        return getVirtualEditionByAcronym(acronym).orElseThrow(LdoDException::new).isPublicOrIsParticipant();
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterOfFragmentForVirtualEdition(String acronym, String xmlId) {
        List<VirtualEditionInter> virtualEditionInters = getVirtualEditionByAcronym(acronym)
                .map(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters()).orElse(new ArrayList<>());
        return virtualEditionInters.stream().map(VirtualEditionInterDto::new).filter(dto -> dto.getFragmentXmlId().equals(xmlId)).collect(Collectors.toSet());
    }

    public Set<VirtualEditionDto> getPublicVirtualEditionsOrUserIsParticipant(String username) {
        return VirtualModule.getInstance().getPublicVirtualEditionsOrUserIsParticipant(username).stream().map(VirtualEditionDto::new).collect(Collectors.toSet());
    }

    public String getVirtualEditionAcronymByVirtualEditionInterXmlId(String interXmlId) {
        return getVirtualEditionInterByXmlId(interXmlId)
                .map(virtualEditionInter -> virtualEditionInter.getVirtualEdition().getAcronym())
                .orElse(null);
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterSet() {
        return VirtualModule.getInstance().getVirtualEditionInterSet().stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet());
    }

    public ScholarInterDto getScholarInterbyExternalId(String interId) {
        DomainObject domainObject = FenixFramework.getDomainObject(interId);

        if (domainObject instanceof VirtualEditionInter) {
            return ((VirtualEditionInter) domainObject).getLastUsed();
        }

        return null;
    }

    public String getVirtualEditionInterTitle(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getTitle).orElse(null);
    }

    public List<String> getVirtualEditionInterCategoryList(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId)
                .map(virtualEditionInter -> virtualEditionInter.getCategories().stream()
                        .map(Category::getName).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }

    public String getVirtualEditionInterExternalId(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getExternalId).orElse(null);
    }

    public String getFragmentXmlIdVirtualEditionInter(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getFragmentXmlId).orElse(null);
    }

    public String getVirtualEditionInterUrlId(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getUrlId).orElse(null);
    }

    public String getVirtualEditionInterReference(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getReference).orElse(null);
    }

    public String getVirtualEditionInterShortName(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getShortName).orElse(null);
    }

    public ScholarInterDto getVirtualEditionLastUsedScholarInter(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getLastUsed).orElse(null);
    }

    public VirtualEditionInterDto getVirtualEditionInterUses(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getUses).map(VirtualEditionInterDto::new).orElse(null);
    }

    public String getUsesVirtualEditionInterId(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getUsesXmlId).orElse(null);
    }

    public Set<String> getTagsForVirtualEditionInter(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId)
                .map(virtualEditionInter -> virtualEditionInter.getTagSet().stream()
                        .map(tag -> tag.getCategory().getName())
                        .collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    public String getVirtualEditionTitleByAcronym(String acronym) {
        return getVirtualEditionByAcronym(acronym).map(virtualEdition -> virtualEdition.getTitle()).orElse(null);
    }

    public List<EditionInterListDto> getPublicVirtualEditionInterListDto() {
        return VirtualModule.getInstance().getVirtualEditionsSet().stream()
                .filter(virtualEdition -> virtualEdition.getPub())
                .map(virtualEdition -> new EditionInterListDto(virtualEdition, false))
                .collect(Collectors.toList());
    }

    public List<String> getVirtualEditionSortedCategoryList(String acronym) {
        return VirtualModule.getInstance()
                .getVirtualEdition(acronym)
                .getTaxonomy()
                .getCategoriesSet().stream()
                .map(Category::getName)
                .sorted()
                .collect(Collectors.toList());
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterDtoSet(String acronym) {
        return VirtualModule.getInstance().getVirtualEdition(acronym).getAllDepthVirtualEditionInters().stream()
                .map(VirtualEditionInterDto::new).collect(Collectors.toSet());
    }

    public int getVirtualEditionInterNumber(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getNumber).orElse(null);
    }

    public List<CategoryDto> getVirtualEditionInterAssignedCategories(String xmlId){
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        List<Category> categories = getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getAssignedCategories).orElse(new ArrayList<>());
        return categories.stream().map(category -> new CategoryDto(category, inter)).collect(Collectors.toList());
    }

    public List<CategoryDto> getVirtualEditionInterAssignedCategoriesForUser(String xmlId, String username){
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        List<Category> categories = getVirtualEditionInterByXmlId(xmlId).map(virtualEditionInter -> virtualEditionInter.getAssignedCategories(username)).orElse(new ArrayList<>());
        return categories.stream().map(category -> new CategoryDto(category, inter)).collect(Collectors.toList());
    }

    public List<CategoryDto> getVirtualEditionInterNonAssignedCategoriesForUser(String xmlId, String username){
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        List<Category> categories = getVirtualEditionInterByXmlId(xmlId).map(virtualEditionInter -> virtualEditionInter.getNonAssignedCategories(username)).orElse(new ArrayList<>());
        return categories.stream().map(category -> new CategoryDto(category, inter)).collect(Collectors.toList());
    }

    public boolean getVirtualEditionTaxonomyVocabularyStatus(String acronym) {
        return getVirtualEditionByAcronym(acronym).map(VirtualEdition::getTaxonomy).map(Taxonomy::getOpenVocabulary).orElse(false);
    }

    public VirtualEditionInterDto getNextVirtualInter(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter.class::cast)
                .map(VirtualEditionInter::getNextNumberInter).map(VirtualEditionInterDto::new).orElse(null);
    }

    public VirtualEditionInterDto getPrevVirtualInter(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter.class::cast)
                .map(VirtualEditionInter::getPrevNumberInter).map(VirtualEditionInterDto::new).orElse(null);
    }

    public List<InterIdDistancePairDto> getIntersByDistance(VirtualEditionInterDto virtualEditionInterDto, WeightsDto weights) {
        VirtualEditionInter virtualEditionInter = getVirtualEditionInterByXmlId(virtualEditionInterDto.getXmlId()).orElse(null);

        if (virtualEditionInter != null) {
            return virtualEditionInter.getVirtualEdition().getIntersByDistance(virtualEditionInter, weights);
        }

        return null;
    }

    public VirtualEditionInterDto getVirtualEditionInterByExternalId(String externalId) {
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof VirtualEditionInter) {
            return new VirtualEditionInterDto((VirtualEditionInter) domainObject);
        }

        return null;
    }

    public List<TagDto> getVirtualEditionInterTags(String xmlId){
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        return inter.getTagsCompleteInter().stream().map(tag -> new TagDto(tag, inter)).collect(Collectors.toList());
    }

    public List<HumanAnnotationDto> getVirtualEditionInterHumanAnnotations(String xmlId){
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        return inter.getAllDepthAnnotations().stream().filter(HumanAnnotation.class::isInstance)
                .map(HumanAnnotation.class::cast)
                .map(annotation -> new HumanAnnotationDto(annotation,inter))
                .collect(Collectors.toList());
    }

    public List<AwareAnnotationDto> getVirtualEditionInterAwareAnnotations(String xmlId){
        return getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new).getAllDepthAnnotations()
                .stream().filter(AwareAnnotation.class::isInstance)
                .map(AwareAnnotation.class::cast)
                .map(AwareAnnotationDto::new)
                .collect(Collectors.toList());
    }

    public void associateVirtualEditionInterCategories(String xmlId, String username, Set<String> categories){
        getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new).associate(username, categories);
    }

    private Optional<VirtualEditionInter> getVirtualEditionInterByXmlId(String xmlId) {
        return VirtualModule.getInstance().getVirtualEditionInterSet().stream()
                .filter(virtualEditionInter -> virtualEditionInter.getXmlId().equals(xmlId)).findAny();
    }

    private Optional<VirtualEdition> getVirtualEditionByAcronym(String acronym) {
        return VirtualModule.getInstance().getVirtualEditionsSet().stream()
                .filter(virtualEdition -> virtualEdition.getAcronym().equals(acronym)).findAny();
    }


}
