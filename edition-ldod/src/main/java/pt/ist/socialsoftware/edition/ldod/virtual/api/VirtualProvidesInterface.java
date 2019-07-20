package pt.ist.socialsoftware.edition.ldod.virtual.api;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.dto.WeightsDto;
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

    public Set<VirtualEditionDto> getVirtualEditions() {
        return VirtualModule.getInstance().getVirtualEditionsSet().stream().map(VirtualEditionDto::new).collect(Collectors.toSet());
    }

    public String getVirtualEditionReference(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getReference).orElse(null);
    }

    public boolean isVirtualEditionPublicOrIsUserParticipant(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).orElseThrow(LdoDException::new).isPublicOrIsParticipant();
    }

    public boolean isUserParticipant(String acronym, String username){
        return getVirtualEditionByAcronymUtil(acronym).orElseThrow(LdoDException::new).getActiveMemberSet()
                .stream().anyMatch(m -> m.getUser().equals(username));
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterOfFragmentForVirtualEdition(String acronym, String xmlId) {
        List<VirtualEditionInter> virtualEditionInters = getVirtualEditionByAcronymUtil(acronym)
                .map(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters()).orElse(new ArrayList<>());
        return virtualEditionInters.stream().map(VirtualEditionInterDto::new).filter(dto -> dto.getFragmentXmlId().equals(xmlId)).collect(Collectors.toSet());
    }

    public Set<VirtualEditionDto> getPublicVirtualEditionsOrUserIsParticipant(String username) {
        return VirtualModule.getInstance().getPublicVirtualEditionsOrUserIsParticipant(username).stream().map(VirtualEditionDto::new).collect(Collectors.toSet());
    }

    public Set<VirtualEditionDto> getVirtualEditionsUserIsParticipant(String username) {
        return VirtualModule.getInstance().getVirtualEditionsUserIsParticipant(username).stream().map(VirtualEditionDto::new).collect(Collectors.toSet());
    }

    public String getVirtualEditionAcronymByVirtualEditionInterXmlId(String interXmlId) {
        return getVirtualEditionInterByXmlId(interXmlId)
                .map(virtualEditionInter -> virtualEditionInter.getVirtualEdition().getAcronym())
                .orElse(null);
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterSet() {
        return VirtualModule.getInstance().getVirtualEditionInterSet().stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet());
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterSet(String acronym) {
        return VirtualModule.getInstance().getVirtualEdition(acronym).getAllDepthVirtualEditionInters().stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet());
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

    public String getVirtualEditionTitleByAcronym(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.getTitle()).orElse(null);
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

    public List<String> getFragmentCategoriesInVirtualEditon(String acronym, String xmlId) {
        return getVirtualEditionByAcronymUtil(acronym).orElse(null).getVirtualEditionInterSetForFragment(xmlId).stream()
                .flatMap(virtualEditionInter -> virtualEditionInter.getCategories().stream())
                .map(category -> category.getName())
                .distinct().collect(Collectors.toList());
    }

    public List<String> getSortedVirtualEditionInterCategories(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).orElse(null).getCategories().stream()
                .map(category -> category.getName()).sorted()
                .collect(Collectors.toList());
    }

    public List<VirtualEditionInterDto> getSortedVirtualEditionInterDtoList(String acronym) {
        return VirtualModule.getInstance().getVirtualEdition(acronym).getAllDepthVirtualEditionInters().stream()
                .map(VirtualEditionInterDto::new).collect(Collectors.toList());
    }

    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEditionDto::new).orElse(null);
    }

    public int getVirtualEditionInterNumber(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getNumber).orElse(null);
    }

    public List<CategoryDto> getVirtualEditionInterAssignedCategories(String xmlId) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        List<Category> categories = getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getAssignedCategories).orElse(new ArrayList<>());
        return categories.stream().map(category -> new CategoryDto(category, inter)).collect(Collectors.toList());
    }

    public List<CategoryDto> getVirtualEditionInterAssignedCategoriesForUser(String xmlId, String username) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        List<Category> categories = getVirtualEditionInterByXmlId(xmlId).map(virtualEditionInter -> virtualEditionInter.getAssignedCategories(username)).orElse(new ArrayList<>());
        return categories.stream().map(category -> new CategoryDto(category, inter)).collect(Collectors.toList());
    }

    public List<CategoryDto> getVirtualEditionInterNonAssignedCategoriesForUser(String xmlId, String username) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        List<Category> categories = getVirtualEditionInterByXmlId(xmlId).map(virtualEditionInter -> virtualEditionInter.getNonAssignedCategories(username)).orElse(new ArrayList<>());
        return categories.stream().map(category -> new CategoryDto(category, inter)).collect(Collectors.toList());
    }

    public boolean getVirtualEditionTaxonomyVocabularyStatus(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getTaxonomy).map(Taxonomy::getOpenVocabulary).orElse(false);
    }

    public boolean getVirtualEditionTaxonomyAnnotationStatus(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getTaxonomy).map(Taxonomy::getOpenAnnotation).orElse(false);
    }

    public VirtualEditionInterDto getNextVirtualInter(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter.class::cast)
                .map(VirtualEditionInter::getNextNumberInter).map(VirtualEditionInterDto::new).orElse(null);
    }

    public VirtualEditionInterDto getPrevVirtualInter(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter.class::cast)
                .map(VirtualEditionInter::getPrevNumberInter).map(VirtualEditionInterDto::new).orElse(null);
    }

    public VirtualEditionInterDto getVirtualEditionInter(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInterDto::new).orElseThrow(LdoDException::new);
    }

    public VirtualEditionInterDto getVirtualEditionInterByExternalId(String externalId) {
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof VirtualEditionInter) {
            return new VirtualEditionInterDto((VirtualEditionInter) domainObject);
        }

        return null;
    }

    public VirtualEditionDto getVirtualEditionByExternalId(String externalId) {
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof VirtualEdition) {
            return new VirtualEditionDto((VirtualEdition) domainObject);
        }

        return null;
    }

    public VirtualEditionDto getVirtualEditionOfVirtualEditionInter(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId)
                .map(virtualEditionInter -> virtualEditionInter.getVirtualEdition())
                .map(VirtualEditionDto::new)
                .orElse(null);
    }

    public String getVirtualEditionExternalIdByAcronym(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getExternalId).orElse(null);
    }

    public List<TagDto> getVirtualEditionInterTags(String xmlId) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        return inter.getTagsCompleteInter().stream().map(tag -> new TagDto(tag, inter)).collect(Collectors.toList());
    }


    public TagDto createTagInInter(String editionId, String interId, String tagName, String user){
        VirtualEditionInter virtualEditionInter = getVirtualEditionInterByXmlId(interId).orElseThrow(LdoDException::new);
        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(editionId);

        Tag tag = edition.getTaxonomy().createTag(virtualEditionInter,tagName,null,user);

        return new TagDto(tag, virtualEditionInter);
    }

    public TagDto getTagInInter(String xmlId, String urlId){
        VirtualEditionInter virtualEditionInter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        Tag tag = virtualEditionInter.getTagSet()
                .stream().filter(t -> t.getCategory().getUrlId().equals(urlId)).findAny().orElse(null);

        if(tag == null){
            return null;
        }

        return new TagDto(tag, virtualEditionInter);
    }

    public CategoryDto getTagCategory(String xmlId, String urlId){
        VirtualEditionInter virtualEditionInter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        Tag tag = virtualEditionInter.getTagSet()
                .stream().filter(t -> t.getCategory().getUrlId().equals(urlId)).findAny().orElse(null);

        if(tag == null){
            return null;
        }

        return new CategoryDto(tag.getCategory(), virtualEditionInter);
    }

    public void removeTagFromInter(String externalId){

        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof Tag) {
            ((Tag) domainObject).remove();
        }
    }

    public List<HumanAnnotationDto> getVirtualEditionInterHumanAnnotations(String xmlId) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        return inter.getAllDepthAnnotations().stream().filter(HumanAnnotation.class::isInstance)
                .map(HumanAnnotation.class::cast)
                .map(annotation -> new HumanAnnotationDto(annotation, inter))
                .collect(Collectors.toList());
    }

    public List<AwareAnnotationDto> getVirtualEditionInterAwareAnnotations(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new).getAllDepthAnnotations()
                .stream().filter(AwareAnnotation.class::isInstance)
                .map(AwareAnnotation.class::cast)
                .map(AwareAnnotationDto::new)
                .collect(Collectors.toList());
    }

    public void associateVirtualEditionInterCategories(String xmlId, String username, Set<String> categories) {
        getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new).associate(username, categories);
    }

    public void dissociateVirtualEditionInterCategory(String xmlId, String username, String categoryName) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        Category category = inter.getAllDepthCategories().stream().filter(cat -> cat.getNameInEditionContext(inter.getEdition()).equals(categoryName))
                .findAny().orElseThrow(LdoDException::new);
        inter.dissociate(username, category);
    }

    public void saveVirtualEdition(String acronym, String[] inters) {
        if (inters != null) {
            VirtualEdition virtualEdition = getVirtualEditionByAcronymUtil(acronym).get();

            List<VirtualEditionInter> virtualEditionInters =
                    Arrays.stream(inters).map(externalId -> (VirtualEditionInter) FenixFramework.getDomainObject(externalId)).collect(Collectors.toList());

            virtualEdition.save(virtualEditionInters);
        }
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createVirtualEdition(String username, String acronym, String title, LocalDate localDate, boolean pub, String[] inters) {
        VirtualEdition virtualEdition = VirtualModule.getInstance().createVirtualEdition(username,
                acronym, title, new LocalDate(), pub, null);

        VirtualEditionInter virtualInter;
        for (int i = 0; i < inters.length; i++) {
            virtualInter = FenixFramework.getDomainObject(inters[i]);
            virtualEdition.createVirtualEditionInter(virtualInter, i + 1);
        }
    }

    public boolean isLdoDEdition(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.isLdoDEdition()).orElse(null);
    }

    public Set<String> getVirtualEditionAdminSet(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.getAdminSet()).orElse(new HashSet<>());
    }

    public Set<String> getVirtualEditionParticipantSet(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.getParticipantSet()).orElse(new HashSet<>());
    }

    public Set<String> getVirtualEditionPendingSet(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym)
                .map(virtualEdition -> virtualEdition.getPendingSet().stream()
                        .map(userDto -> userDto.getUsername()).collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    public boolean getVirtualEditionPub(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.getPub()).orElse(false);
    }

    public LocalDate getVirtualEditionDate(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.getDate()).orElse(null);
    }

    private Optional<VirtualEditionInter> getVirtualEditionInterByXmlId(String xmlId) {
        return VirtualModule.getInstance().getVirtualEditionInterSet().stream()
                .filter(virtualEditionInter -> virtualEditionInter.getXmlId().equals(xmlId)).findAny();
    }

    private Optional<VirtualEdition> getVirtualEditionByAcronymUtil(String acronym) {
        return VirtualModule.getInstance().getVirtualEditionsSet().stream()
                .filter(virtualEdition -> virtualEdition.getAcronym().equals(acronym)).findAny();
    }


}



