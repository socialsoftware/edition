package pt.ist.socialsoftware.edition.virtual.api;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.virtual.api.textDto.FragmentDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.ScholarInterDto;
import pt.ist.socialsoftware.edition.virtual.domain.*;


import pt.ist.socialsoftware.edition.virtual.api.dto.*;
import pt.ist.socialsoftware.edition.virtual.feature.inout.GenerateTEIFragmentsCorpus;
import pt.ist.socialsoftware.edition.virtual.feature.inout.VirtualEditionFragmentsTEIImport;
import pt.ist.socialsoftware.edition.virtual.feature.inout.VirtualEditionsTEICorpusImport;
import pt.ist.socialsoftware.edition.virtual.feature.inout.WriteVirtualEditonsToFile;
import pt.ist.socialsoftware.edition.virtual.feature.socialaware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.virtual.feature.socialaware.CitationDetecter;
import pt.ist.socialsoftware.edition.virtual.feature.socialaware.FetchCitationsFromTwitter;
import pt.ist.socialsoftware.edition.virtual.feature.socialaware.TweetFactory;
import pt.ist.socialsoftware.edition.virtual.feature.topicmodeling.TopicModeler;
import pt.ist.socialsoftware.edition.virtual.utils.*;


import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class VirtualProvidesInterface {
    private static final Logger logger = LoggerFactory.getLogger(VirtualProvidesInterface.class);

    private static Map<String, String> virtualEditionMap = new HashMap<>();

    public static void cleanVirtualEditionMapCache() {
        virtualEditionMap = new HashMap<>();
    }

    private static Map<String, String> virtualEditionInterMapByXmlId = new HashMap<>();

    public static void cleanVirtualEditionInterMapByXmlIdCache() {
        virtualEditionInterMapByXmlId = new HashMap<>();
    }

    private static Map<String, String> virtualEditionInterMapByUrlId = new HashMap<>();

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
                        .filter(virtualEditionInter -> virtualEditionInter
                                .getFragmentXmlId()
                                .equals(xmlId))
                        .map(VirtualEditionInterDto::new)
                        .collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    public Set<VirtualEditionDto> getPublicVirtualEditionsOrUserIsParticipant(String username) {
        return VirtualModule.getInstance().getPublicVirtualEditionsOrUserIsParticipant(username).stream().map(VirtualEditionDto::new).collect(Collectors.toSet());
    }

    public Set<VirtualEditionInterDto> getVirtualEditionIntersUserIsContributor(String username) {
        return VirtualModule.getInstance().getVirtualEditionIntersUserIsContributor(username).stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet());
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

    public List<VirtualEditionInterListDto> getPublicVirtualEditionInterListDto() {
        return VirtualModule.getInstance().getVirtualEditionsSet().stream()
                .filter(virtualEdition -> virtualEdition.getPub())
                .map(virtualEdition -> new VirtualEditionInterListDto(virtualEdition, false))
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

    public List<String> getSortedVirtualEditionInterCategoriesName(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).orElse(null).getCategories().stream()
                .map(category -> category.getName()).sorted()
                .collect(Collectors.toList());
    }

    public List<CategoryDto> getSortedVirtualEditionInterCategoriesFromVirtualEdition(String xmlId, String acronym) {
        return getVirtualEditionInterByXmlId(xmlId).orElse(null).getCategories().stream()
                .filter(c -> c.getTaxonomy().getEdition().getAcronym() == acronym)
                .map(CategoryDto::new).sorted()
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
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (virtualEdition != null) {
            return virtualEdition.getTaxonomy().getOpenVocabulary();
        }
        return false;
    }

    public boolean getVirtualEditionTaxonomyAnnotationStatus(String acronym) {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (virtualEdition != null) {
            return virtualEdition.getTaxonomy().getOpenAnnotation();
        }
        return false;
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

    public VirtualEditionInterDto getVirtualEditionInterFromModule(String xmlId) {
        VirtualEditionInter inter = VirtualModule.getInstance().getVirtualEditionInterByXmlId(xmlId);

        if (inter != null) {
            return new VirtualEditionInterDto(inter);
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

    public void associateVirtualEditionInterCategoriesbyExternalId(String externalId, String username, Set<String> categories) {
        VirtualEditionInter inter = FenixFramework.getDomainObject(externalId);

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
    public void createVirtualEdition(String username, String acronym, String title, LocalDate localDate, boolean pub, String acronymOfUsed, String[] inters) {
        VirtualEdition virtualEdition = VirtualModule.getInstance().createVirtualEdition(username,
                VirtualEdition.ACRONYM_PREFIX + acronym, title, new LocalDate(), pub, acronymOfUsed);

        if (inters != null) {
            VirtualEditionInter virtualInter;
            for (int i = 0; i < inters.length; i++) {
                virtualInter = FenixFramework.getDomainObject(inters[i]);
                virtualEdition.createVirtualEditionInter(virtualInter, i + 1);
            }
        }
    }

    public boolean isLdoDEdition(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.isLdoDEdition()).orElse(null);
    }

    public Set<String> getVirtualEditionAdminSet(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.getAdminSet()).orElse(new HashSet<>());
    }

    public Set<String> getVirtualEditionAdminSetByExternalId(String external) {
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(external);
       if (virtualEdition != null) {
           return virtualEdition.getAdminSet();
       }
        return new HashSet<>();
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

    public TaxonomyDto getVirtualEditionTaxonomy(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getTaxonomy).map(TaxonomyDto::new).orElse(null);
    }

    public void loadTEICorpusVirtual(InputStream file) {
        new VirtualEditionsTEICorpusImport().loadTEICorpusVirtual(file);
    }

    public void loadTEIFragmentCorpus(Set<FragmentDto> fragments) {
        new GenerateTEIFragmentsCorpus().LoadFragmentCorpus(fragments);
    }

    public Set<CategoryDto> getCategoriesFromTaxonomy(String acronym) {
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.getTaxonomy().getCategoriesSet().stream()
                .map(CategoryDto::new)
                .collect(Collectors.toSet()))
                .orElse(new HashSet<>());
        //        return VirtualModule.getInstance().getVirtualEdition(acronym).getTaxonomy().getCategoriesSet().stream().map(CategoryDto::new).collect(Collectors.toSet());
    }

    public List<VirtualEditionInterDto> getSortedInterFromCategoriesTag(String externalId) {
        Category category = FenixFramework.getDomainObject(externalId);
        if (category != null) {
            return category.getSortedInters().stream().map(VirtualEditionInterDto::new).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<VirtualEditionInterDto> getSortedInterFromCategoriesTag(String externalId, String acronym) {
        Category category = FenixFramework.getDomainObject(externalId);
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (category != null && virtualEdition != null) {
            return category.getSortedInters(virtualEdition).stream().map(VirtualEditionInterDto::new).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<String> getSortedUsersFromCategoriesTag(String externalId) {
        Category category = FenixFramework.getDomainObject(externalId);
        if (category != null) {
            return category.getSortedUsers();
        }
        return new ArrayList<>();
    }

    public List<VirtualEditionDto> getSortedEditionsFromCategoriesTag(String externalId) {
        Category category = FenixFramework.getDomainObject(externalId);
        if (category != null) {
            return category.getSortedEditions().stream().map(VirtualEditionDto::new).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public Set<String> getContributorSetFromVirtualEditionInter(String externalId, String xmlId ,String username) {
        Category category = FenixFramework.getDomainObject(externalId);
        VirtualEditionInter inter = VirtualModule.getInstance().getVirtualEditionInterByXmlId(xmlId);

        if (category != null && inter != null) {
            Set<Tag> tagsAccessibleByUser = inter.getAllDepthTagsAccessibleByUser(username);
            return category.getTagSet().stream()
                    .filter(tag -> tagsAccessibleByUser.contains(tag))
                    .map(t -> t.getContributor())
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    public Set<Range> getRangeSetFromAnnotation(String externalId) {
        Annotation annotation = FenixFramework.getDomainObject(externalId);
        if (annotation != null) {
            return annotation.getRangeSet();
        }
        return new HashSet<>();
    }

    public HumanAnnotationDto createHumanAnnotation(String xmlId, String quote, String text, String user, List<RangeJson> ranges, List<String> tags) {
        VirtualEditionInter inter = VirtualModule.getInstance().getVirtualEditionInterByXmlId(xmlId);
        if (inter != null) {
            return new HumanAnnotationDto(inter.createHumanAnnotation(quote, text, user, ranges, tags), inter);
        }
        return null;
    }

    public HumanAnnotationDto getHumanAnnotationfromId(String Id) {
        HumanAnnotation annotation = FenixFramework.getDomainObject(Id);
        if (annotation != null) {
            return new HumanAnnotationDto(annotation, annotation.getVirtualEditionInter());
        }
        return null;
    }

    public boolean canUserUpdateHumanAnnotation(String Id, String user) {
        HumanAnnotation annotation = FenixFramework.getDomainObject(Id);
        if (annotation != null) {
            return annotation.canUpdate(user);
        }
        return false;
    }

    public HumanAnnotationDto updateHumanAnnotation(String id, String text, List<String> tags) {
        HumanAnnotation annotation = FenixFramework.getDomainObject(id);
        if (annotation != null) {
            annotation.update(text, tags);
            return new HumanAnnotationDto(annotation, annotation.getVirtualEditionInter());
        }
        return null;
    }

    public boolean canUserDeleteHumanAnnotation(String id, String user) {
        HumanAnnotation annotation = FenixFramework.getDomainObject(id);
        if (annotation != null) {
            return annotation.canDelete(user);
        }
        return false;
    }

    public void removeHumanAnnotation(String id) {
        HumanAnnotation annotation = FenixFramework.getDomainObject(id);
        if (annotation != null) {
            annotation.remove();
        }
    }

    public VirtualEditionDto getArchiveVirtualEdition() {
        VirtualEdition archive = VirtualModule.getInstance().getArchiveEdition();
        if (archive != null) {
            return new VirtualEditionDto(VirtualModule.getInstance().getArchiveEdition());
        }
        return null;
    }

    public void editVirtualEdition(String xmlId, String acronym, String title, String synopsis, boolean pub, boolean management, boolean vocabulary, boolean annotation, String mediaSource, String beginDate, String endDate, String geoLocation, String frequency) {
       VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEditionByXmlId(xmlId);
        if (virtualEdition != null) {
            virtualEdition.edit(VirtualEdition.ACRONYM_PREFIX + acronym, title, synopsis, pub, management, vocabulary, annotation, mediaSource, beginDate, endDate, geoLocation, frequency);
        }
    }

    public void searchForAwareAnnotations(String externalId) {

        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition != null) {
            AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();
            if (virtualEdition.isSAVE()) {
                awareFactory.searchForAwareAnnotations(virtualEdition);
            }
            // this virtual edition is not SAVE anymore, therefore we have to remove all the
            // aware annotations
            else {
                for (VirtualEditionInter inter : virtualEdition.getAllDepthVirtualEditionInters()) {
                    awareFactory.removeAllAwareAnnotationsFromVEInter(inter);
                }
            }
        }
    }

    public void removeVirtualEditionByExternalId(String externalId) {
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            ((VirtualEdition) virtualEdition).remove();
        }
    }

    public void updateVirtualEditionInters(String externalId, List<String> fragIntersXmlIds) {
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            ((VirtualEdition) virtualEdition).updateVirtualEditionInters(fragIntersXmlIds);
        }
    }

    public void addMemberByExternalId(String externalId, String user, boolean b) {
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            ((VirtualEdition) virtualEdition).addMember(user, Member.MemberRole.MEMBER, b);
        }
    }

    public void cancelParticipationSubmissionByExternalId(String externalId, String user) {
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            ((VirtualEdition) virtualEdition).cancelParticipationSubmission(user);
        }
    }

    public void addApproveByExternalId(String externalId, String username) {
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            ((VirtualEdition) virtualEdition).addApprove(username);
        }
    }

    public boolean canSwitchRole(String externalId, String authenticatedUser, String username) {
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            return ((VirtualEdition) virtualEdition).canSwitchRole(authenticatedUser, username);
        }
       return false;
    }

    public void switchRole(String externalId, String username) {
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            ((VirtualEdition) virtualEdition).switchRole(username);
        }
    }

    public boolean canRemoveMember(String externalId, String authenticatedUser, String user) {
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            return ((VirtualEdition) virtualEdition).canRemoveMember(authenticatedUser, user);
        }
        return false;
    }


    public void removeMember(String externalId, String user) {
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            ((VirtualEdition) virtualEdition).removeMember(user);
        }
    }

    public VirtualEditionInterDto createVirtualEditionInterFromScholarInter(String externalId, String xmlId, int max) {
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        ScholarInterDto scholarInterDto = VirtualRequiresInterface.getInstance().getScholarInterByXmlId(xmlId);
        if (virtualEdition instanceof VirtualEdition && scholarInterDto != null) {
            return new VirtualEditionInterDto(((VirtualEdition) virtualEdition).createVirtualEditionInter(scholarInterDto, max));
        }
        return null;
    }

    public VirtualEditionInterDto createVirtualEditionInterFromVirtualEditionInter(String externalId, String interExternalId, int max) {
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        DomainObject inter = FenixFramework.getDomainObject(interExternalId);
        if (virtualEdition instanceof VirtualEdition && inter instanceof VirtualEditionInter) {
            return new VirtualEditionInterDto(((VirtualEdition) virtualEdition).createVirtualEditionInter((VirtualEditionInter) inter , max));
        }
        return null;
    }

    public void editTaxonomy(String editionAcronym, boolean management, boolean vocabulary, boolean annotation) {
        getVirtualEditionByAcronymUtil(editionAcronym).map(VirtualEdition_Base::getTaxonomy).ifPresent(taxonomy -> taxonomy.edit(management, vocabulary, annotation));
    }

    public void generateTopicModeler(String username, String externalId, int numTopics, int numWords, int thresholdCategories, int numIterations) throws IOException {
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            TopicModeler topicModeler = new TopicModeler();
            topicModeler.generate(username, (VirtualEdition) virtualEdition, numTopics, numWords, thresholdCategories, numIterations);
        }
    }

    public void createGeneratedCategories(String editionAcronym, TopicListDTO topicList) {
//        VirtualModule.getInstance().getVirtualEdition(editionAcronym).getTaxonomy().createGeneratedCategories(topicList);
        getVirtualEditionByAcronymUtil(editionAcronym).map(VirtualEdition_Base::getTaxonomy).ifPresent(taxonomy -> taxonomy.createGeneratedCategories(topicList));
    }

    public TaxonomyDto getTaxonomyByExternalId(String externalId) {
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);
        if (domainObject instanceof Taxonomy) {
            return new TaxonomyDto((Taxonomy) domainObject);
        }
        return null;
    }

    public void removeTaxonomy(String editionAcronym) {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(editionAcronym);
        if (virtualEdition != null) {
            virtualEdition.getTaxonomy().remove();
            virtualEdition.setTaxonomy(new Taxonomy());
        }
    }

    public void createCategory(String editionAcronym, String name) {
        getVirtualEditionByAcronymUtil(editionAcronym).map(VirtualEdition_Base::getTaxonomy).ifPresent(taxonomy -> taxonomy.createCategory(name));
    }

    public CategoryDto getCategoryByExternalId(String externalId) {
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof Category) {
            return new CategoryDto((Category) domainObject);
        }

        return null;
    }

    public void updateCategoryNameByExternalId(String externalId, String name) {
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);
        if (domainObject instanceof Category) {
            ((Category) domainObject).setName(name);
        }
    }

    public void removeCategory(String externalId) {
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof Category) {
            ((Category) domainObject).remove();
        }

    }

    public CategoryDto mergeCategories(String editionAcronym, List<CategoryDto> categories) {
        List<Category> categoryList = categories.stream().map(categoryDto -> (Category) FenixFramework.getDomainObject(categoryDto.getExternalId())).collect(Collectors.toList());
        return new CategoryDto(VirtualModule.getInstance().getVirtualEdition(editionAcronym).getTaxonomy().merge(categoryList));
    }

    public void deleteCategories(String editionAcronym, List<CategoryDto> categories) {
        List<Category> categoryList = categories.stream().map(categoryDto -> (Category) FenixFramework.getDomainObject(categoryDto.getExternalId())).collect(Collectors.toList());
        VirtualModule.getInstance().getVirtualEdition(editionAcronym).getTaxonomy().delete(categoryList);
    }

    public CategoryDto extractCategories(String editionAcronym, String externalId, String[] interIds) {
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof Category) {
            Category category = ((Category) domainObject);
            Set<VirtualEditionInter> inters = Arrays.stream(interIds).map(s -> (VirtualEditionInter) FenixFramework.getDomainObject(s)).collect(Collectors.toSet());
            return new CategoryDto(VirtualModule.getInstance().getVirtualEdition(editionAcronym).getTaxonomy().extract(category, inters));
        }
        return null;

    }

    public String getMediaSourceName(String acronym) {

//        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(acronym);
//        if (edition != null && edition.getMediaSource() != null) {
//            return edition.getMediaSource().getName();
//        }
//        return null;
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition:: getMediaSource).map(MediaSource::getName).orElse(null);

    }

    public LocalDate getTimeWindowBeginDate(String acronym) {

//        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(acronym);
//        if (edition != null && edition.getTimeWindow() != null ) {
//            return edition.getTimeWindow().getBeginDate();
//        }
//        return null;
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getTimeWindow).map(TimeWindow::getBeginDate).orElse(null);
    }

    public LocalDate getTimeWindowEndDate(String acronym) {
//        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(acronym);
//        if (edition != null && edition.getTimeWindow() != null ) {
//            return edition.getTimeWindow().getEndDate();
//        }
//        return null;
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getTimeWindow).map(TimeWindow::getEndDate).orElse(null);
    }

    public boolean containsEveryCountryinGeographicLocation(String acronym) {
//        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(acronym);
//        if (edition != null && edition.getGeographicLocation() != null ) {
//            return edition.getGeographicLocation().containsEveryCountry();
//        }
//        return false;
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getGeographicLocation).map(GeographicLocation::containsEveryCountry).orElse(false);
    }

    public boolean containsCountryinGeographicLocation(String acronym, String country) {
//        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(acronym);
//        if (edition != null && edition.getGeographicLocation() != null ) {
//            return edition.getGeographicLocation().containsCountry(country);
//        }
//        return false;
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getGeographicLocation)
                .map(geographicLocation -> geographicLocation.containsCountry(country)).orElse(false);

    }

    public int getIntegerFrequency(String acronym) {
//        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(acronym);
//        if (edition != null && edition.getFrequency() != null ) {
//            return edition.getFrequency().getFrequency();
//        }
//        return 0;
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getFrequency).map(Frequency_Base::getFrequency).orElse(0);
    }

    public Set<MemberDto> getActiveMembersFromVirtualEdition(String acronym) {
        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (edition != null) {
            return edition.getActiveMemberSet().stream().map(MemberDto::new).collect(Collectors.toSet());
        }

        return new HashSet<>();
    }

    public Set<MemberDto> getPendingMemberFromVirtualEdition(String acronym) {
        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (edition != null ) {
            return edition.getPendingMemberSet().stream().map(MemberDto::new).collect(Collectors.toSet());
        }

        return new HashSet<>();
    }

    public List<VirtualEditionDto> getTaxonomyUsedIn(String editionAcronym) {
        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(editionAcronym);
        if (edition != null) {
            return edition.getTaxonomy().getUsedIn().stream().map(VirtualEditionDto::new).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public String getWriteVirtualEditionToFileExport() throws IOException {
        WriteVirtualEditonsToFile write = new WriteVirtualEditonsToFile();
        return write.export();
    }

    public void importVirtualEditionCorpus(InputStream inputStream) {
        VirtualEditionsTEICorpusImport loader = new VirtualEditionsTEICorpusImport();
        loader.importVirtualEditionsCorpus(inputStream);
    }

    public String importVirtualEditionFragmentFromTEI(InputStream inputStream) {
        VirtualEditionFragmentsTEIImport loader = new VirtualEditionFragmentsTEIImport();
        return loader.importFragmentFromTEI(inputStream);
    }

    public List<TwitterCitationDto> getAllTwitterCitations() {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        return  VirtualModule.getInstance().getAllTwitterCitation().stream()
                .sorted((c1, c2) -> java.time.LocalDateTime.parse(c2.getDate(), formater)
                        .compareTo(java.time.LocalDateTime.parse(c1.getDate(), formater)))
                .map(TwitterCitationDto::new).collect(Collectors.toList());
    }

    public Set<TweetDto> getAllTweets() {
        return VirtualModule.getInstance().getTweetSet().stream().map(TweetDto::new).collect(Collectors.toSet());
    }

    public void removeTweets() {
        VirtualModule.getInstance().removeTweets();
    }

    public void detectCitation() throws IOException {
        CitationDetecter detecter = new CitationDetecter();
        detecter.detect();
    }

    public void createTweetFactory() throws IOException {
        TweetFactory tweetFactory = new TweetFactory();
        tweetFactory.create();
    }

    public void generateAwareAnnotations() throws IOException {
        AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();
        awareFactory.generate();
    }

    public void dailyRegenerateTwitterCitationEdition() {
        VirtualModule.dailyRegenerateTwitterCitationEdition();
    }

    public List<String> getAnnotationTextListFromVirtualEdition(String acronym) {
        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (edition != null && !edition.getAnnotationTextList().isEmpty()) {
            return edition.getAnnotationTextList();
        }
        return new ArrayList<>();
    }

    public List<CategoryDTO> getVirtualEditionInterAllDepthCategoriesAccessibleByUser(String xmlId, String username) {
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        return inter.getAllDepthCategoriesAccessibleByUser(username).stream()
                .sorted((c1, c2) -> c1.compareInEditionContext(inter.getVirtualEdition(), c2))
                .map(c -> new CategoryDTO(inter.getVirtualEdition(), c)).collect(Collectors.toList());
    }

    public boolean canCreateHumanAnnotationOnVirtualEdition(String acronym, String username) {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (virtualEdition != null) {
            return HumanAnnotation.canCreate(virtualEdition, username);
        }
        return false;
    }

    public VirtualEditionInterDto getVirtualEditionFragInterByUrlId(String acronym, String urlId) {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (virtualEdition != null) {
            return virtualEdition.getIntersSet().stream().filter(i -> i.getUrlId().equals(urlId)).findFirst().map(VirtualEditionInterDto::new).orElse(null);
        }
        return null;
    }

    public VirtualEditionInterListDto getVirtualEditionInterList(String acronym, boolean deep) {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (virtualEdition != null) {
            return new VirtualEditionInterListDto(virtualEdition, deep);
        }
        return null;
    }

    public List<VirtualEditionDto> getVirtualEditionsUserIsParticipantSelectedOrPublic(String username) {
        return VirtualModule.getInstance().getVirtualEditionsUserIsParticipantSelectedOrPublic(username).stream().map(VirtualEditionDto::new).collect(Collectors.toList());
    }




    private Optional<VirtualEditionInter> getVirtualEditionInterByXmlId(String xmlId) {
        if (xmlId == null) {
            return Optional.empty();
        }

        String virtualEditionInterInter = virtualEditionInterMapByXmlId.get(xmlId);

        if (virtualEditionInterInter == null) {
            virtualEditionInterInter = VirtualModule.getInstance().getVirtualEditionInterSet().stream()
                    .filter(vei -> vei.getXmlId().equals(xmlId))
                    .findAny()
                    .map(vi -> vi.getExternalId())
                    .orElse(null);

            if (virtualEditionInterInter != null) {
                virtualEditionInterMapByXmlId.put(xmlId, virtualEditionInterInter);
            }
        }

        return Optional.ofNullable(virtualEditionInterInter != null ? FenixFramework.getDomainObject(virtualEditionInterInter) : null);
    }

    private Optional<VirtualEditionInter> getVirtualEditionInterByUrlIdUtil(String urlId) {
        if (urlId == null) {
            return Optional.empty();
        }

        String virtualEditionInterId = virtualEditionInterMapByUrlId.get(urlId);

        if (virtualEditionInterId == null) {
            virtualEditionInterId = VirtualModule.getInstance().getVirtualEditionInterSet().stream()
                    .filter(vei -> vei.getUrlId().equals(urlId))
                    .findAny()
                    .map(vei -> vei.getExternalId())
                    .orElse(null);

            if (virtualEditionInterId != null) {
               virtualEditionInterMapByUrlId.put(urlId, virtualEditionInterId);
            }
        }

        return Optional.ofNullable(virtualEditionInterId != null ? FenixFramework.getDomainObject(virtualEditionInterId) : null);
    }

    private Optional<VirtualEdition> getVirtualEditionByAcronymUtil(String acronym) {
        if (acronym == null) {
            return Optional.empty();
        }

        String virtualEditionId = virtualEditionMap.get(acronym);

        if (virtualEditionId == null) {
            virtualEditionId = VirtualModule.getInstance().getVirtualEditionsSet().stream()
                    .filter(ve -> ve.getAcronym().equals(acronym))
                    .findAny()
                    .map(ve -> ve.getExternalId())
                    .orElse(null);

            if (virtualEditionId != null) {
                virtualEditionMap.put(acronym, virtualEditionId);
            }
        }

        return Optional.ofNullable(virtualEditionId != null ? FenixFramework.getDomainObject(virtualEditionId) : null);
    }


    public boolean initializeVirtualModule() {
        return VirtualBootstrap.initializeVirtualModule();
    }

    public void fetchCitationsFromTwitter() throws IOException {
        FetchCitationsFromTwitter fetch = new FetchCitationsFromTwitter();
        fetch.fetch();
    }
}




