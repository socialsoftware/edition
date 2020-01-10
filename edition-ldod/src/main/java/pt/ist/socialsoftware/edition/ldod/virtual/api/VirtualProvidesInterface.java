package pt.ist.socialsoftware.edition.ldod.virtual.api;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.*;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionInterListDto;

import java.util.*;
import java.util.stream.Collectors;

public class VirtualProvidesInterface {
    private static final Logger logger = LoggerFactory.getLogger(VirtualProvidesInterface.class);

    private static Map<String, VirtualEdition> virtualEditionMap = new HashMap<>();

    public static void cleanVirtualEditionMapCache() {
        virtualEditionMap = new HashMap<>();
    }

    private static Map<String, VirtualEditionInter> virtualEditionInterMapByXmlId = new HashMap<>();

    public static void cleanVirtualEditionInterMapByXmlIdCache() {
        virtualEditionInterMapByXmlId = new HashMap<>();
    }

    private static Map<String, VirtualEditionInter> virtualEditionInterMapByUrlId = new HashMap<>();

    public static void cleanVirtualEditionInterMapByUrlIdCache() {
        virtualEditionInterMapByUrlId = new HashMap<>();
    }

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

    public boolean isVirtualEditionPublicOrIsUserParticipant(String acronym, String username) {
        return getVirtualEditionByAcronymUtil(acronym).orElseThrow(LdoDException::new).isPublicOrIsParticipant(username);
    }

    public boolean isUserParticipant(String acronym, String username) {
        return getVirtualEditionByAcronymUtil(acronym).orElseThrow(LdoDException::new).getActiveMemberSet()
                .stream().anyMatch(m -> m.getUser().equals(username));
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterOfFragmentForVirtualEdition(String acronym, String xmlId) {
        return getVirtualEditionByAcronymUtil(acronym)
                .map(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream()
                        .filter(virtualEditionInter -> virtualEditionInter.getFragmentXmlId().equals(xmlId))
                        .map(VirtualEditionInterDto::new)
                        .collect(Collectors.toSet()))
                .orElse(new HashSet<>());
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

    public VirtualEditionInterDto getVirtualEditionInterByUrlId(String urlId) {
        return getVirtualEditionInterByUrlIdUtil(urlId).map(VirtualEditionInterDto::new).orElse(null);
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

    public List<CategoryDto> getCategoriesUsedInTags(String xmlId, String username) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        List<Category> categories = getVirtualEditionInterByXmlId(xmlId)
                .map(virtualEditionInter -> virtualEditionInter.getAllDepthCategoriesUsedInTags(username)).orElse(new ArrayList<>());
        return categories.stream().map(category -> new CategoryDto(category, inter, username)).collect(Collectors.toList());
    }

    public List<CategoryDto> getVirtualEditionInterAllDepthCategoriesUsedInTags(String xmlId, String username) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        return getVirtualEditionInterByXmlId(xmlId)
                .map(virtualEditionInter -> virtualEditionInter.getAllDepthCategoriesUsedInTags(username).stream()
                        .map(category -> new CategoryDto(category, inter, username)).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }

    public List<CategoryDto> getVirtualEditionInterAllDepthCategoriesUsedByUserInTags(String xmlId, String username) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        List<Category> categories = getVirtualEditionInterByXmlId(xmlId).map(virtualEditionInter -> virtualEditionInter.getAllDepthCategoriesUsedByUserInTags(username)).orElse(new ArrayList<>());
        return categories.stream().map(category -> new CategoryDto(category, inter, username)).collect(Collectors.toList());
    }

    public List<CategoryDto> getVirtualEditionInterAllDepthCategoriesNotUsedInTags(String xmlId, String username) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        List<Category> categories = getVirtualEditionInterByXmlId(xmlId)
                .map(virtualEditionInter -> virtualEditionInter.getAllDepthCategoriesNotUsedInTags(username)).orElse(new ArrayList<>());
        return categories.stream().map(category -> new CategoryDto(category, inter, username)).collect(Collectors.toList());
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

    public List<TagDto> getAllDepthTagsNotHumanAnnotationAccessibleByUser(String xmlId, String username) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        return inter.getAllDepthTagsNotHumanAnnotationAccessibleByUser(username).stream().map(tag -> new TagDto(tag, inter)).collect(Collectors.toList());
    }


    public TagDto createTagInInter(String editionId, String interId, String tagName, String user) {
        VirtualEditionInter virtualEditionInter = getVirtualEditionInterByXmlId(interId).orElseThrow(LdoDException::new);
        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(editionId);

        Tag tag = edition.getTaxonomy().createTag(virtualEditionInter, tagName, null, user);

        return new TagDto(tag, virtualEditionInter);
    }

    public TagDto getTagInInter(String xmlId, String urlId) {
        logger.debug(xmlId);
        logger.debug(urlId);

        VirtualEditionInter virtualEditionInter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        Tag tag = virtualEditionInter.getTagSet()
                .stream().filter(t -> t.getCategory().getUrlId().equals(urlId)).findAny().orElse(null);

        if (tag == null) {
            return null;
        }

        return new TagDto(tag, virtualEditionInter);
    }

    public CategoryDto getTagCategory(String xmlId, String urlId) {
        VirtualEditionInter virtualEditionInter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        Tag tag = virtualEditionInter.getTagSet()
                .stream().filter(t -> t.getCategory().getUrlId().equals(urlId)).findAny().orElse(null);

        if (tag == null) {
            return null;
        }

        return new CategoryDto(tag.getCategory(), virtualEditionInter, null);
    }

    public void removeTagFromInter(String externalId) {

        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof Tag) {
            ((Tag) domainObject).remove();
        }
    }

    public List<AnnotationDto> getAllDepthAnnotationsAccessibleByUser(String xmlId, String username) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        return inter.getAllDepthAnnotationsAccessibleByUser(username).stream()
                .map(annotation -> {
                    if (annotation instanceof HumanAnnotation) {
                        return new HumanAnnotationDto((HumanAnnotation) annotation, inter);
                    } else {
                        return new AwareAnnotationDto((AwareAnnotation) annotation);
                    }
                })
                .collect(Collectors.toList());
    }

    public List<HumanAnnotationDto> getVirtualEditionInterHumanAnnotationsAccessibleByUser(String xmlId, String username) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        return inter.getAllDepthAnnotationsAccessibleByUser(username).stream().filter(HumanAnnotation.class::isInstance)
                .map(HumanAnnotation.class::cast)
                .map(annotation -> new HumanAnnotationDto(annotation, inter))
                .collect(Collectors.toList());
    }

    public List<AwareAnnotationDto> getVirtualEditionInterAwareAnnotationsAccessibleByUser(String xmlId, String username) {
        return getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new).getAllDepthAnnotationsAccessibleByUser(username)
                .stream().filter(AwareAnnotation.class::isInstance)
                .map(AwareAnnotation.class::cast)
                .map(AwareAnnotationDto::new)
                .collect(Collectors.toList());
    }

    public void associateVirtualEditionInterCategories(String xmlId, String username, Set<String> categories) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);

        if (username == null || !inter.getVirtualEdition().isPublicOrIsParticipant(username)) {
            throw new LdoDException("not authorized");
        }

        inter.associate(username, categories);
    }

    public void dissociateVirtualEditionInterCategory(String xmlId, String username, String categoryExternalId) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        Category category = FenixFramework.getDomainObject(categoryExternalId);
        if (category == null) {
            throw new LdoDException();
        }

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

    public List<String> getSelectedVirtualEditionsByUser(String username) {
        return VirtualModule.getInstance().getUserSelectedVirtualEditions(username);
    }

    public void addToUserSelectedVirtualEditions(String username, List<String> selectedAcronyms) {
        VirtualModule.getInstance().addToUserSelectedVirtualEditions(username, selectedAcronyms);
    }

    public void removeVirtualEditionSelectedByUser(String user, String virtualEditionAcronym) {
        getVirtualEditionByAcronymUtil(virtualEditionAcronym).get().removeSelectedByUser(user);
    }

    public void addVirtualEditionSelectedByUser(String user, String virtualEditionAcronym) {
        getVirtualEditionByAcronymUtil(virtualEditionAcronym).get().addSelectedByUser(user);

    }

    public boolean canAddFragInter(String acronym, String interXmlId) {
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.canAddFragInter(interXmlId)).orElse(false);
    }


    public boolean canManipulateAnnotation(String acronym, String username) {
        return getVirtualEditionByAcronymUtil(acronym)
                .map(virtualEdition -> virtualEdition.getTaxonomy().canManipulateAnnotation(username))
                .orElse(false);
    }

    public boolean getOpenVocabulary(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym)
                .map(virtualEdition -> virtualEdition.getTaxonomy().getOpenVocabulary())
                .orElse(false);
    }


    public String getAllDepthCategoriesJSON(String xmlId, String username) {
        return getVirtualEditionInterByXmlId(xmlId)
                .map(virtualEditionInter -> virtualEditionInter.getAllDepthCategoriesJSON(username))
                .orElse(null);
    }

    public Set<TagDto> getVirtualEditionInterAllDepthTagsAccessibleByUser(String xmlId, String username) {
        VirtualEditionInter virtualEditionInter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(() -> new LdoDException());
        return getVirtualEditionInterByXmlId(xmlId)
                .map(vei -> vei.getAllDepthTagsAccessibleByUser(username).stream()
                        .map(tag -> new TagDto(tag, virtualEditionInter))
                        .collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    public Set<TagDto> getAllTags(String xmlId) {
        VirtualEditionInter virtualEditionInter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(() -> new LdoDException());
        return getVirtualEditionInterByXmlId(xmlId)
                .map(vei -> vei.getTagSet().stream()
                        .map(tag -> new TagDto(tag, virtualEditionInter))
                        .collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    public VirtualEditionDto getVirtualEditionOfTaxonomyByExternalId(String externalId) {
        Taxonomy taxonomy = FenixFramework.getDomainObject(externalId);
        if (taxonomy != null) {
            return new VirtualEditionDto(taxonomy.getEdition());
        }
        return null;
    }

    public VirtualEditionDto getVirtualEditionOfCategoryByExternalId(String externalId) {
        Category category = FenixFramework.getDomainObject(externalId);
        if (category != null) {
            return new VirtualEditionDto(category.getTaxonomy().getEdition());
        }
        return null;
    }

    public VirtualEditionDto getVirtualEditionOfTagByExternalId(String externalId) {
        Tag tag = FenixFramework.getDomainObject(externalId);
        if (tag != null) {
            return new VirtualEditionDto(tag.getInter().getEdition());
        }
        return null;
    }

    public boolean canManipulateTaxonomy(String acronym, String username) {
        return getVirtualEditionByAcronymUtil(acronym)
                .map(virtualEdition -> virtualEdition.getTaxonomy()
                        .canManipulateTaxonomy(username)).orElse(false);
    }


    private Optional<VirtualEditionInter> getVirtualEditionInterByXmlId(String xmlId) {
        if (xmlId == null) {
            return Optional.empty();
        }

        VirtualEditionInter virtualEditionInter = virtualEditionInterMapByXmlId.get(xmlId);

        if (virtualEditionInter == null) {
            virtualEditionInter = VirtualModule.getInstance().getVirtualEditionInterSet().stream()
                    .filter(vei -> vei.getXmlId().equals(xmlId))
                    .findAny()
                    .orElse(null);

            if (virtualEditionInter != null) {
                virtualEditionInterMapByXmlId.put(xmlId, virtualEditionInter);
            }
        }

        return Optional.ofNullable(virtualEditionInter);
    }

    private Optional<VirtualEditionInter> getVirtualEditionInterByUrlIdUtil(String urlId) {
        if (urlId == null) {
            return Optional.empty();
        }

        VirtualEditionInter virtualEditionInter = virtualEditionInterMapByUrlId.get(urlId);

        if (virtualEditionInter == null) {
            virtualEditionInter = VirtualModule.getInstance().getVirtualEditionInterSet().stream()
                    .filter(vei -> vei.getUrlId().equals(urlId))
                    .findAny()
                    .orElse(null);

            if (virtualEditionInter != null) {
                virtualEditionInterMapByUrlId.put(urlId, virtualEditionInter);
            }
        }

        return Optional.ofNullable(virtualEditionInter);
    }

    private Optional<VirtualEdition> getVirtualEditionByAcronymUtil(String acronym) {
        if (acronym == null) {
            return Optional.empty();
        }

        VirtualEdition virtualEdition = virtualEditionMap.get(acronym);

        if (virtualEdition == null) {
            virtualEdition = VirtualModule.getInstance().getVirtualEditionsSet().stream()
                    .filter(ve -> ve.getAcronym().equals(acronym))
                    .findAny()
                    .orElse(null);

            if (virtualEdition != null) {
                virtualEditionMap.put(acronym, virtualEdition);
            }
        }

        return Optional.ofNullable(virtualEdition);
    }

}




