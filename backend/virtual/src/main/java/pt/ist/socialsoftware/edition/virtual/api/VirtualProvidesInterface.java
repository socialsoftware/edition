package pt.ist.socialsoftware.edition.virtual.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.notification.dtos.text.CitationDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.TopicListDTO;
import pt.ist.socialsoftware.edition.notification.utils.LdoDException;

import pt.ist.socialsoftware.edition.virtual.domain.*;


import pt.ist.socialsoftware.edition.virtual.api.dto.*;
import pt.ist.socialsoftware.edition.virtual.feature.inout.*;
import pt.ist.socialsoftware.edition.virtual.feature.socialaware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.virtual.feature.socialaware.CitationDetecter;
import pt.ist.socialsoftware.edition.virtual.feature.socialaware.FetchCitationsFromTwitter;
import pt.ist.socialsoftware.edition.virtual.feature.socialaware.TweetFactory;
import pt.ist.socialsoftware.edition.virtual.feature.topicmodeling.TopicModeler;
import pt.ist.socialsoftware.edition.virtual.utils.*;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class VirtualProvidesInterface {
    private static final Logger logger = LoggerFactory.getLogger(VirtualProvidesInterface.class);

    private static Map<String, String> virtualEditionMap = new HashMap<>();

    @PostMapping("/cleanVirtualEditionMapCache")
    public static void cleanVirtualEditionMapCache() {
        virtualEditionMap = new HashMap<>();
    }

    private static Map<String, String> virtualEditionInterMapByXmlId = new HashMap<>();

    @PostMapping("/cleanVirtualEditionInterMapByXmlIdCache")
    public static void cleanVirtualEditionInterMapByXmlIdCache() {
        virtualEditionInterMapByXmlId = new HashMap<>();
    }

    private static Map<String, String> virtualEditionInterMapByUrlId = new HashMap<>();

    @PostMapping("/cleanVirtualEditionInterMapByUrlIdCache")
    public static void cleanVirtualEditionInterMapByUrlIdCache() {
        virtualEditionInterMapByUrlId = new HashMap<>();
    }

    @GetMapping("/isInterInVirtualEdition")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean isInterInVirtualEdition(@RequestParam(name = "xmlId") String xmlId, @RequestParam(name = "acronym") String acronym) {
       logger.debug("isInterInVirtualEdition: " + xmlId + ", " + acronym);
        return VirtualModule.getInstance().getVirtualEdition(acronym).getAllDepthVirtualEditionInters().stream()
                .anyMatch(virtualEditionInter -> virtualEditionInter.getXmlId().equals(xmlId));
    }

    @GetMapping("/virtualEdition/{acronym}")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionDto getVirtualEdition(@PathVariable("acronym") String acronym) {
       logger.debug("getVirtualEdition: " + acronym);
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (virtualEdition != null) {
            return new VirtualEditionDto(virtualEdition);
        }

        return null;
    }

    @GetMapping("/virtualEditions")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<VirtualEditionDto> getVirtualEditions() {
       logger.debug("getVirtualEditions");
        return VirtualModule.getInstance().getVirtualEditionsSet().stream().map(VirtualEditionDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/virtualEdition/{acronym}/reference")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getVirtualEditionReference(@PathVariable("acronym") String acronym) {
       logger.debug("getVirtualEditionReference: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getReference).orElse(null);
    }

    @GetMapping("/virtualEdition/{acronym}/isPublicOrIsUserParticipant")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean isVirtualEditionPublicOrIsUserParticipant(@PathVariable("acronym") String acronym, @RequestParam(name = "username") String username) {
       logger.debug("isVirtualEditionPublicOrIsUserParticipant: " + acronym + ", " + username);
        return getVirtualEditionByAcronymUtil(acronym).orElseThrow(LdoDException::new).isPublicOrIsParticipant(username);
    }

    @GetMapping("/virtualEdition/{acronym}/isUserParticipant")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean isUserParticipant(@PathVariable("acronym") String acronym, @RequestParam(name = "username") String username) {
       logger.debug("isUserParticipant: " + acronym + ", " + username);
        return getVirtualEditionByAcronymUtil(acronym).orElseThrow(LdoDException::new).getActiveMemberSet()
                .stream().anyMatch(m -> m.getUser().equals(username));
    }

    @GetMapping("/virtualEdition/{acronym}/virtualEditionInterOfFragment")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<VirtualEditionInterDto> getVirtualEditionInterOfFragmentForVirtualEdition(@PathVariable("acronym") String acronym, @RequestParam(name = "xmlId") String xmlId) {
       logger.debug("getVirtualEditionInterOfFragmentForVirtualEdition: " + acronym + ", " + xmlId);
        return getVirtualEditionByAcronymUtil(acronym)
                .map(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream()
                        .filter(virtualEditionInter -> virtualEditionInter
                                .getFragmentXmlId()
                                .equals(xmlId))
                        .map(VirtualEditionInterDto::new)
                        .collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    @GetMapping("/virtualEditions/getPublicVirtualEditionsOrUserIsParticipant")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<VirtualEditionDto> getPublicVirtualEditionsOrUserIsParticipant(@RequestParam(name = "username") String username) {
       logger.debug("getPublicVirtualEditionsOrUserIsParticipant: " + username);
        return VirtualModule.getInstance().getPublicVirtualEditionsOrUserIsParticipant(username).stream().map(VirtualEditionDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/virtualEditionsInters/getVirtualEditionIntersUserIsContributor")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<VirtualEditionInterDto> getVirtualEditionIntersUserIsContributor(@RequestParam(name = "username") String username) {
       logger.debug("getVirtualEditionIntersUserIsContributor: " + username);
        return VirtualModule.getInstance().getVirtualEditionIntersUserIsContributor(username).stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/virtualEditionsInters/getVirtualEditionIntersUserIsParticipant")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<VirtualEditionDto> getVirtualEditionsUserIsParticipant(@RequestParam(name = "username") String username) {
       logger.debug("getVirtualEditionsUserIsParticipant: " + username);
        return VirtualModule.getInstance().getVirtualEditionsUserIsParticipant(username).stream().map(VirtualEditionDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/virtualEditionsInter/{interXmlId}/virtualEditionAcronym")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getVirtualEditionAcronymByVirtualEditionInterXmlId(@PathVariable("interXmlId") String interXmlId) {
       logger.debug("getVirtualEditionAcronymByVirtualEditionInterXmlId: " + interXmlId);
        return getVirtualEditionInterByXmlId(interXmlId)
                .map(virtualEditionInter -> virtualEditionInter.getVirtualEdition().getAcronym())
                .orElse(null);
    }

    @GetMapping("/virtualEditionInterSet")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<VirtualEditionInterDto> getVirtualEditionInterSet() {
       logger.debug("getVirtualEditionInterSet");
        return VirtualModule.getInstance().getVirtualEditionInterSet().stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/virtualEditionInterSet/{acronym}")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<VirtualEditionInterDto> getVirtualEditionInterSet(@PathVariable("acronym") String acronym) {
       logger.debug("getVirtualEditionInterSet: " + acronym);
        return VirtualModule.getInstance().getVirtualEdition(acronym).getAllDepthVirtualEditionInters().stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/scholarInter/ext/{interId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public ScholarInterDto getScholarInterbyExternalId(String interId) {
       logger.debug("getScholarInterbyExternalId: " + interId);
        DomainObject domainObject = FenixFramework.getDomainObject(interId);

        if (domainObject instanceof VirtualEditionInter) {
            return ((VirtualEditionInter) domainObject).getLastUsed();
        }

        return null;
    }

    @GetMapping("/virtualEditionInter/{xmlId}/title")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getVirtualEditionInterTitle(@PathVariable("xmlId") String xmlId) {
       logger.debug("getVirtualEditionInterTitle: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getTitle).orElse(null);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/externalId")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getVirtualEditionInterExternalId(@PathVariable("xmlId") String xmlId) {
       logger.debug("getVirtualEditionInterExternalId: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getExternalId).orElse(null);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/fragmentXmlId")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getFragmentXmlIdVirtualEditionInter(@PathVariable("xmlId") String xmlId) {
       logger.debug("getFragmentXmlIdVirtualEditionInter: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getFragmentXmlId).orElse(null);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/urlId")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getVirtualEditionInterUrlId(@PathVariable("xmlId") String xmlId) {
       logger.debug("getVirtualEditionInterUrlId: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getUrlId).orElse(null);
    }

    @GetMapping("/virtualEditionInter/urlId/{urlId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionInterDto getVirtualEditionInterByUrlId(@PathVariable("urlId") String urlId) {
       logger.debug("getVirtualEditionInterByUrlId: " + urlId);
        return getVirtualEditionInterByUrlIdUtil(urlId).map(VirtualEditionInterDto::new).orElse(null);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/reference")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getVirtualEditionInterReference(@PathVariable("xmlId") String xmlId) {
       logger.debug("getVirtualEditionInterReference: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getReference).orElse(null);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/shortName")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getVirtualEditionInterShortName(@PathVariable("xmlId") String xmlId) {
       logger.debug("getVirtualEditionInterShortName: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getShortName).orElse(null);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/lastUsed")
    @Atomic(mode = Atomic.TxMode.READ)
    public ScholarInterDto getVirtualEditionLastUsedScholarInter(@PathVariable("xmlId") String xmlId) {
       logger.debug("getVirtualEditionLastUsedScholarInter: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getLastUsed).orElse(null);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/uses")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionInterDto getVirtualEditionInterUses(@PathVariable("xmlId") String xmlId) {
       logger.debug("getVirtualEditionInterUses: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getUses).map(VirtualEditionInterDto::new).orElse(null);
    }

    @GetMapping("/virtualEdition/{acronym}/title")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getVirtualEditionTitleByAcronym(@PathVariable("acronym") String acronym) {
       logger.debug("getVirtualEditionTitleByAcronym: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.getTitle()).orElse(null);
    }

    @GetMapping("/publicVirtualEditionInterList")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<VirtualEditionInterListDto> getPublicVirtualEditionInterListDto() {
       logger.debug("getPublicVirtualEditionInterListDto");
        return VirtualModule.getInstance().getVirtualEditionsSet().stream()
                .filter(virtualEdition -> virtualEdition.getPub())
                .map(virtualEdition -> new VirtualEditionInterListDto(virtualEdition, false))
                .collect(Collectors.toList());
    }

    @GetMapping("/virtualEdition/{acronym}/sortedCategory")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> getVirtualEditionSortedCategoryList(@PathVariable("acronym") String acronym) {
       logger.debug("getVirtualEditionSortedCategoryList: " + acronym);
        return VirtualModule.getInstance()
                .getVirtualEdition(acronym)
                .getTaxonomy()
                .getCategoriesSet().stream()
                .map(Category::getName)
                .sorted()
                .collect(Collectors.toList());
    }

    @GetMapping("/virtualEdition/{acronym}/fragmentCategories")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> getFragmentCategoriesInVirtualEditon(@PathVariable("acronym") String acronym, @RequestParam(name = "xmlId") String xmlId) {
       logger.debug("getFragmentCategoriesInVirtualEditon: " + acronym + ", " + xmlId);
        return getVirtualEditionByAcronymUtil(acronym).orElse(null).getVirtualEditionInterSetForFragment(xmlId).stream()
                .flatMap(virtualEditionInter -> virtualEditionInter.getCategories().stream())
                .map(category -> category.getName())
                .distinct().collect(Collectors.toList());
    }

    @GetMapping("/virtualEditionInter/{xmlId}/sortedCategoriesName")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> getSortedVirtualEditionInterCategoriesName(@PathVariable("xmlId") String xmlId) {
       logger.debug("getSortedVirtualEditionInterCategoriesName: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId).orElse(null).getCategories().stream()
                .map(category -> category.getName()).sorted()
                .collect(Collectors.toList());
    }

    @GetMapping("/virtualEdition/{acronym}/sortedVirtualEditionInterCategories")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<CategoryDto> getSortedVirtualEditionInterCategoriesFromVirtualEdition(@RequestParam(name = "xmlId") String xmlId, @PathVariable("acronym") String acronym) {
       logger.debug("getSortedVirtualEditionInterCategoriesFromVirtualEdition: " + xmlId + ", " + acronym);
        return getVirtualEditionInterByXmlId(xmlId).orElse(null).getCategories().stream()
                .filter(c -> c.getTaxonomy().getEdition().getAcronym() == acronym)
                .map(CategoryDto::new).sorted()
                .collect(Collectors.toList());
    }

    @GetMapping("/virtualEdition/{acronym}/sortedVirtualEditionInterList")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<VirtualEditionInterDto> getSortedVirtualEditionInterDtoList(@PathVariable("acronym") String acronym) {
       logger.debug("getSortedVirtualEditionInterDtoList: " + acronym);
        return VirtualModule.getInstance().getVirtualEdition(acronym).getAllDepthVirtualEditionInters().stream()
                .map(VirtualEditionInterDto::new).collect(Collectors.toList());
    }

    @GetMapping("/virtualEdition/acronym/{acronym}")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionDto getVirtualEditionByAcronym(@PathVariable("acronym") String acronym) {
       logger.debug("getVirtualEditionByAcronym: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEditionDto::new).orElse(null);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/number")
    @Atomic(mode = Atomic.TxMode.READ)
    public int getVirtualEditionInterNumber(@PathVariable("xmlId") String xmlId) {
       logger.debug("getVirtualEditionInterNumber: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getNumber).orElse(null);
    }

    @GetMapping("/categoriesUsedInTags")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<CategoryDto> getCategoriesUsedInTags(@RequestParam(name = "xmlId") String xmlId, @RequestParam(name = "username") String username) {
       logger.debug("getCategoriesUsedInTags: " + xmlId + ", " + username);
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        List<Category> categories = getVirtualEditionInterByXmlId(xmlId)
                .map(virtualEditionInter -> virtualEditionInter.getAllDepthCategoriesUsedInTags(username)).orElse(new ArrayList<>());
        return categories.stream().map(category -> new CategoryDto(category, inter, username)).collect(Collectors.toList());
    }

    @GetMapping("/virtualEditionInter/{xmlId}/allDepthCategoriesUsedInTags")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<CategoryDto> getVirtualEditionInterAllDepthCategoriesUsedInTags(@PathVariable("xmlId") String xmlId, @RequestParam(name = "username") String username) {
       logger.debug("getVirtualEditionInterAllDepthCategoriesUsedInTags: " + xmlId + ", " + username);
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        return getVirtualEditionInterByXmlId(xmlId)
                .map(virtualEditionInter -> virtualEditionInter.getAllDepthCategoriesUsedInTags(username).stream()
                        .map(category -> new CategoryDto(category, inter, username)).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }


    @GetMapping("/virtualEditionInter/{xmlId}/allDepthCategoriesUsedByUserInTags")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<CategoryDto> getVirtualEditionInterAllDepthCategoriesUsedByUserInTags(@PathVariable("xmlId") String xmlId, @RequestParam(name = "username") String username) {
       logger.debug("getVirtualEditionInterAllDepthCategoriesUsedByUserInTags:" + xmlId + ", " + username );
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        List<Category> categories = getVirtualEditionInterByXmlId(xmlId).map(virtualEditionInter -> virtualEditionInter.getAllDepthCategoriesUsedByUserInTags(username)).orElse(new ArrayList<>());
        return categories.stream().map(category -> new CategoryDto(category, inter, username)).collect(Collectors.toList());
    }

    @GetMapping("/virtualEditionInter/{xmlId}/allDepthCategoriesNotUsedInTags")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<CategoryDto> getVirtualEditionInterAllDepthCategoriesNotUsedInTags(@PathVariable("xmlId") String xmlId, @RequestParam(name = "username") String username) {
       logger.debug("getVirtualEditionInterAllDepthCategoriesNotUsedInTags: " + xmlId + ", " + username);
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        List<Category> categories = getVirtualEditionInterByXmlId(xmlId)
                .map(virtualEditionInter -> virtualEditionInter.getAllDepthCategoriesNotUsedInTags(username)).orElse(new ArrayList<>());
        return categories.stream().map(category -> new CategoryDto(category, inter, username)).collect(Collectors.toList());
    }

    @GetMapping("/virtualEdition/{acronym}/taxonomyVocabularyStatus")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean getVirtualEditionTaxonomyVocabularyStatus(@PathVariable("acronym") String acronym) {
       logger.debug("getVirtualEditionTaxonomyVocabularyStatus: " + acronym);
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (virtualEdition != null) {
            return virtualEdition.getTaxonomy().getOpenVocabulary();
        }
        return false;
    }

    @GetMapping("/virtualEdition/{acronym}/taxonomyAnnotationStatus")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean getVirtualEditionTaxonomyAnnotationStatus(@PathVariable("acronym") String acronym) {
       logger.debug("getVirtualEditionTaxonomyAnnotationStatus: " + acronym);
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (virtualEdition != null) {
            return virtualEdition.getTaxonomy().getOpenAnnotation();
        }
        return false;
    }

    @GetMapping("/virtualEditionInter/{xmlId}/nextInter")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionInterDto getNextVirtualInter(@PathVariable("xmlId") String xmlId) {
       logger.debug("getNextVirtualInter: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter.class::cast)
                .map(VirtualEditionInter::getNextNumberInter).map(VirtualEditionInterDto::new).orElse(null);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/prevInter")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionInterDto getPrevVirtualInter(@PathVariable("xmlId") String xmlId) {
       logger.debug("getPrevVirtualInter: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter.class::cast)
                .map(VirtualEditionInter::getPrevNumberInter).map(VirtualEditionInterDto::new).orElse(null);
    }

    @GetMapping("/virtualEditionInter/xmlId/{xmlId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionInterDto getVirtualEditionInter(@PathVariable("xmlId") String xmlId) {
       logger.debug("getVirtualEditionInter: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInterDto::new).orElseThrow(LdoDException::new);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/interFromModule")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionInterDto getVirtualEditionInterFromModule(@PathVariable("xmlId") String xmlId) {
       logger.debug("getVirtualEditionInterFromModule: " + xmlId);
        VirtualEditionInter inter = VirtualModule.getInstance().getVirtualEditionInterByXmlId(xmlId);

        if (inter != null) {
            return new VirtualEditionInterDto(inter);
        }
        return null;
    }

    @GetMapping("/virtualEditionInter/ext/{externalId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionInterDto getVirtualEditionInterByExternalId(@PathVariable("externalId") String externalId) {
       logger.debug("getVirtualEditionInterByExternalId: " + externalId);
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof VirtualEditionInter) {
            return new VirtualEditionInterDto((VirtualEditionInter) domainObject);
        }

        return null;
    }

    @GetMapping("/virtualEdition/ext/{externalId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionDto getVirtualEditionByExternalId(@PathVariable("externalId") String externalId) {
       logger.debug("getVirtualEditionByExternalId: " + externalId);
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof VirtualEdition) {
            return new VirtualEditionDto((VirtualEdition) domainObject);
        }

        return null;
    }

    @GetMapping("/virtualEditionInter/{xmlId}/virtualEdition")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionDto getVirtualEditionOfVirtualEditionInter(@PathVariable("xmlId") String xmlId) {
       logger.debug("getVirtualEditionOfVirtualEditionInter: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId)
                .map(virtualEditionInter -> virtualEditionInter.getVirtualEdition())
                .map(VirtualEditionDto::new)
                .orElse(null);
    }

    @GetMapping("/virtualEdition/{acronym}/externalId")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getVirtualEditionExternalIdByAcronym(@PathVariable("acronym") String acronym) {
       logger.debug("getVirtualEditionExternalIdByAcronym: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getExternalId).orElse(null);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/allDepthTagsNotHumanAnnotationAccessibleByUser")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<TagDto> getAllDepthTagsNotHumanAnnotationAccessibleByUser(@PathVariable("xmlId") String xmlId, @RequestParam(name = "username") String username) {
       logger.debug("getAllDepthTagsNotHumanAnnotationAccessibleByUser: " + xmlId + ", " + username);
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        return inter.getAllDepthTagsNotHumanAnnotationAccessibleByUser(username).stream().map(tag -> new TagDto(tag, inter)).collect(Collectors.toList());
    }


    @PostMapping("/createTagInInter")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public TagDto createTagInInter(@RequestParam(name = "editionId") String editionId, @RequestParam(name = "interId") String interId, @RequestParam(name = "tagName") String tagName, @RequestParam(name = "user") String user) {
       logger.debug("createTagInInter: " + editionId);
        VirtualEditionInter virtualEditionInter = getVirtualEditionInterByXmlId(interId).orElseThrow(LdoDException::new);
        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(editionId);

        Tag tag = edition.getTaxonomy().createTag(virtualEditionInter, tagName, null, user);

        return new TagDto(tag, virtualEditionInter);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/tag")
    @Atomic(mode = Atomic.TxMode.READ)
    public TagDto getTagInInter(@PathVariable("xmlId") String xmlId, @RequestParam(name = "urlId") String urlId) {
        logger.debug(xmlId);
        logger.debug(urlId);
       logger.debug("getTagInInter: " + xmlId);

        VirtualEditionInter virtualEditionInter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        Tag tag = virtualEditionInter.getTagSet()
                .stream().filter(t -> t.getCategory().getUrlId().equals(urlId)).findAny().orElse(null);

        if (tag == null) {
            return null;
        }

        return new TagDto(tag, virtualEditionInter);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/tagCategory")
    @Atomic(mode = Atomic.TxMode.READ)
    public CategoryDto getTagCategory(@PathVariable("xmlId") String xmlId, @RequestParam(name = "urlId") String urlId) {
       logger.debug("getTagCategory: " + xmlId);
        VirtualEditionInter virtualEditionInter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        Tag tag = virtualEditionInter.getTagSet()
                .stream().filter(t -> t.getCategory().getUrlId().equals(urlId)).findAny().orElse(null);

        if (tag == null) {
            return null;
        }

        return new CategoryDto(tag.getCategory(), virtualEditionInter, null);
    }

    @PostMapping("/removeTagFromInter")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeTagFromInter(@RequestParam(name = "externalId") String externalId) {
       logger.debug("removeTagFromInter: " + externalId);
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof Tag) {
            ((Tag) domainObject).remove();
        }
    }

    @GetMapping("/virtualEditionInter/{xmlId}/allDepthAnnotationsAccessibleByUser")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<AnnotationDto> getAllDepthAnnotationsAccessibleByUser(@PathVariable("xmlId") String xmlId, @RequestParam(name = "username") String username) {
       logger.debug("getAllDepthAnnotationsAccessibleByUser: " + xmlId);
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

    @GetMapping("/virtualEditionInter/{xmlId}/humanAnnotationsAccessibleByUser")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<HumanAnnotationDto> getVirtualEditionInterHumanAnnotationsAccessibleByUser(@PathVariable("xmlId") String xmlId, @RequestParam(name = "username") String username) {
       logger.debug("getVirtualEditionInterHumanAnnotationsAccessibleByUser: " + xmlId);
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        return inter.getAllDepthAnnotationsAccessibleByUser(username).stream().filter(HumanAnnotation.class::isInstance)
                .map(HumanAnnotation.class::cast)
                .map(annotation -> new HumanAnnotationDto(annotation, inter))
                .collect(Collectors.toList());
    }

    @GetMapping("/virtualEditionInter/{xmlId}/awareAnnotationsAccessibleByUser")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<AwareAnnotationDto> getVirtualEditionInterAwareAnnotationsAccessibleByUser(@PathVariable("xmlId") String xmlId, @RequestParam(name = "username") String username) {
       logger.debug("getVirtualEditionInterAwareAnnotationsAccessibleByUser: " + xmlId + ", " + username);
        return getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new).getAllDepthAnnotationsAccessibleByUser(username)
                .stream().filter(AwareAnnotation.class::isInstance)
                .map(AwareAnnotation.class::cast)
                .map(AwareAnnotationDto::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/associateVirtualEditionInterCategories")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void associateVirtualEditionInterCategories(@RequestParam(name = "xmlId") String xmlId, @RequestParam(name = "username") String username, @RequestParam(name = "categories") Set<String> categories) {
       logger.debug("associateVirtualEditionInterCategories: " + xmlId + ", " + username + ", " + categories);
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);

        if (username == null || !inter.getVirtualEdition().isPublicOrIsParticipant(username)) {
            throw new LdoDException("not authorized");
        }

        inter.associate(username, categories);
    }

    @PostMapping("/associateVirtualEditionInterCategoriesByExternalId")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void associateVirtualEditionInterCategoriesbyExternalId(@RequestParam(name = "externalId") String externalId, @RequestParam(name = "username") String username, @RequestParam(name = "categories") Set<String> categories) {
       logger.debug("associateVirtualEditionInterCategoriesByExternalId: " + externalId + ", " + username + ", " + categories);
        VirtualEditionInter inter = FenixFramework.getDomainObject(externalId);

        inter.associate(username, categories);
    }

    @PostMapping("/dissociateVirtualEditionInterCategory")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void dissociateVirtualEditionInterCategory(@RequestParam(name = "xmlId") String xmlId, @RequestParam(name = "username") String username, @RequestParam(name = "categoryExternalId") String categoryExternalId) {
      logger.debug("dissociateVirtualEditionInterCategory: " + xmlId + ", " + username + ", " + categoryExternalId);
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        Category category = FenixFramework.getDomainObject(categoryExternalId);
        if (category == null) {
            throw new LdoDException();
        }

        inter.dissociate(username, category);
    }

    @PostMapping("/saveVirtualEdition")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void saveVirtualEdition(@RequestParam(name = "acronym") String acronym, @RequestParam(name = "inters") String[] inters) {
       logger.debug("saveVirtualEdition: " + acronym + ", " + inters);
        if (inters != null) {
            VirtualEdition virtualEdition = getVirtualEditionByAcronymUtil(acronym).get();

            List<VirtualEditionInter> virtualEditionInters =
                    Arrays.stream(inters).map(externalId -> (VirtualEditionInter) FenixFramework.getDomainObject(externalId)).collect(Collectors.toList());

            virtualEdition.save(virtualEditionInters);
            virtualEditionMap.replace(acronym, virtualEdition.getExternalId());
        }
    }


    @PostMapping("/createVirtualEdition")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createVirtualEdition(@RequestParam(name = "username") String username, @RequestParam(name = "acronym") String acronym,
                                     @RequestParam(name = "title") String title, @RequestParam(name = "pub") boolean pub,
                                     @RequestParam(name = "acronymOfUsed", required = false) String acronymOfUsed, @RequestParam(name = "inters", required = false) String[] inters) {
       logger.debug("createVirtualEdition: " + username + ", " + acronym);

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

    @GetMapping("/isLdoDEdition")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean isLdoDEdition(@RequestParam(name = "acronym") String acronym) {
       logger.debug("isLdoDEdition: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.isLdoDEdition()).orElse(null);
    }

    @GetMapping("/virtualEdition/{acronym}/adminSet")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<String> getVirtualEditionAdminSet(@PathVariable(name = "acronym") String acronym) {
       logger.debug("getVirtualEditionAdminSet: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.getAdminSet()).orElse(new HashSet<>());
    }

    @GetMapping("/virtualEdition/{external}/adminSetByExternalId")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<String> getVirtualEditionAdminSetByExternalId(@PathVariable("external") String external) {
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(external);
       if (virtualEdition != null) {
           return virtualEdition.getAdminSet();
       }
        return new HashSet<>();
    }

    @GetMapping("/virtualEdition/{acronym}/participants")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<String> getVirtualEditionParticipantSet(@PathVariable("acronym") String acronym) {
       logger.debug("getVirtualEditionParticipantSet: " + acronym);
//        cleanVirtualEditionMapCache();
        return VirtualModule.getInstance().getVirtualEdition(acronym).getParticipantSet();
//        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.getParticipantSet()).orElse(new HashSet<>());
    }


    @GetMapping("/virtualEdition/{acronym}/pending")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<String> getVirtualEditionPendingSet(@PathVariable("acronym") String acronym) {
       logger.debug("getVirtualEditionPendingSet: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym)
                .map(virtualEdition -> virtualEdition.getPendingSet().stream()
                        .map(userDto -> userDto.getUsername()).collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    @GetMapping("/virtualEdition/{acronym}/pub")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean getVirtualEditionPub(@PathVariable("acronym") String acronym) {
       logger.debug("getVirtualEditionPub: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.getPub()).orElse(false);
    }

    @GetMapping("/virtualEdition/{acronym}/date")
    @Atomic(mode = Atomic.TxMode.READ)
    public LocalDate getVirtualEditionDate(@PathVariable("acronym") String acronym) {
       logger.debug("getVirtualEditionDate: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.getDate()).orElse(null);
    }

    @GetMapping("/userSelectedVirtualEditions")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> getSelectedVirtualEditionsByUser(@RequestParam(name = "username") String username) {
       logger.debug("userSelectedVirtualEditions: " + username);
        return VirtualModule.getInstance().getUserSelectedVirtualEditions(username);
    }

    @PostMapping("/addToUserSelectedVirtualEditions")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void addToUserSelectedVirtualEditions(@RequestParam(name = "username") String username, @RequestParam(name = "selectedAcronyms") List<String> selectedAcronyms) {
       logger.debug("addToUserSelectedVirtualEditions: " + username);
        VirtualModule.getInstance().addToUserSelectedVirtualEditions(username, selectedAcronyms);
    }

    @PostMapping("/removeVirtualEditionSelectedByUser")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeVirtualEditionSelectedByUser(@RequestParam(name = "user") String user, @RequestParam(name = "virtualEditionAcronym") String virtualEditionAcronym) {
       logger.debug("removeVirtualEditionSelectedByUser: " + user);
        getVirtualEditionByAcronymUtil(virtualEditionAcronym).get().removeSelectedByUser(user);
    }

    @PostMapping("/addVirtualEditionSelectedByUser")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void addVirtualEditionSelectedByUser(@RequestParam(name = "user") String user, @RequestParam(name = "virtualEditionAcronym") String virtualEditionAcronym) {
       logger.debug("addVirtualEditionSelectedByUser: " + user);
        getVirtualEditionByAcronymUtil(virtualEditionAcronym).get().addSelectedByUser(user);
    }

    @GetMapping("/virtualEdition/{acronym}/canAddFragInter")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean canAddFragInter(@PathVariable("acronym") String acronym, @RequestParam(name = "interXmlId") String interXmlId) {
       logger.debug("canAddFragInter: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.canAddFragInter(interXmlId)).orElse(false);
    }

    @GetMapping("/virtualEdition/{acronym}/canManipulateAnnotation")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean canManipulateAnnotation(@PathVariable("acronym") String acronym, @RequestParam(name = "username") String username) {
       logger.debug("canManipulateAnnotation: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym)
                .map(virtualEdition -> virtualEdition.getTaxonomy().canManipulateAnnotation(username))
                .orElse(false);
    }

    @GetMapping("/virtualEdition/{acronym}/openVocabulary")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean getOpenVocabulary(@PathVariable("acronym") String acronym) {
       logger.debug("getOpenVocabulary: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym)
                .map(virtualEdition -> virtualEdition.getTaxonomy().getOpenVocabulary())
                .orElse(false);
    }


    @GetMapping("/virtualEditionInter/{xmlId}/allDepthCategoriesJSON")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getAllDepthCategoriesJSON(@PathVariable("xmlId") String xmlId, @RequestParam(name = "username") String username) {
       logger.debug("getAllDepthCategoriesJSON: " + xmlId);
        return getVirtualEditionInterByXmlId(xmlId)
                .map(virtualEditionInter -> virtualEditionInter.getAllDepthCategoriesJSON(username))
                .orElse(null);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/allDepthTagsAcessibleByUser")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<TagDto> getVirtualEditionInterAllDepthTagsAccessibleByUser(@PathVariable("xmlId") String xmlId, @RequestParam(name = "username") String username) {
       logger.debug("getVirtualEditionInterAllDepthTagsAccessibleByUser: " + xmlId);
        VirtualEditionInter virtualEditionInter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(() -> new LdoDException());
        return getVirtualEditionInterByXmlId(xmlId)
                .map(vei -> vei.getAllDepthTagsAccessibleByUser(username).stream()
                        .map(tag -> new TagDto(tag, virtualEditionInter))
                        .collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    @GetMapping("/virtualEditionInter/{xmlId}/allTags")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<TagDto> getAllTags(@PathVariable("xmlId") String xmlId) {
       logger.debug("getAllTags: " + xmlId);
        VirtualEditionInter virtualEditionInter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(() -> new LdoDException());
        return getVirtualEditionInterByXmlId(xmlId)
                .map(vei -> vei.getTagSet().stream()
                        .map(tag -> new TagDto(tag, virtualEditionInter))
                        .collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    @GetMapping("/taxonomy/{externalId}/virtualEdition")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionDto getVirtualEditionOfTaxonomyByExternalId(@PathVariable("externalId") String externalId) {
       logger.debug("getVirtualEditionOfTaxonomyByExternalId: " + externalId);
        Taxonomy taxonomy = FenixFramework.getDomainObject(externalId);
        if (taxonomy != null) {
            return new VirtualEditionDto(taxonomy.getEdition());
        }
        return null;
    }

    @GetMapping("/category/{externalId}/virtualEdition")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionDto getVirtualEditionOfCategoryByExternalId(@PathVariable("externalId") String externalId) {
       logger.debug("getVirtualEditionOfCategoryByExternalId: " + externalId);
        Category category = FenixFramework.getDomainObject(externalId);
        if (category != null) {
            return new VirtualEditionDto(category.getTaxonomy().getEdition());
        }
        return null;
    }

    @GetMapping("/tag/{externalId}/virtualEdition")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionDto getVirtualEditionOfTagByExternalId(@PathVariable("externalId") String externalId) {
       logger.debug("getVirtualEditionOfTagByExternalId: " + externalId);
        Tag tag = FenixFramework.getDomainObject(externalId);
        if (tag != null) {
            return new VirtualEditionDto(tag.getInter().getEdition());
        }
        return null;
    }

    @GetMapping("/virtualEdition/{acronym}/canManipulateTaxonomy")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean canManipulateTaxonomy(@PathVariable("acronym") String acronym, @RequestParam(name = "username") String username) {
       logger.debug("canManipulateTaxonomy: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym)
                .map(virtualEdition -> virtualEdition.getTaxonomy()
                        .canManipulateTaxonomy(username)).orElse(false);
    }

    @GetMapping("/virtualEdition/{acronym}/taxonomy")
    @Atomic(mode = Atomic.TxMode.READ)
    public TaxonomyDto getVirtualEditionTaxonomy(@PathVariable("acronym") String acronym) {
       logger.debug("getVirtualEditionTaxonomy: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getTaxonomy).map(TaxonomyDto::new).orElse(null);
    }

    @PostMapping("/loadTEICorpusVirtual")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void loadTEICorpusVirtual(@RequestBody byte[] inputStream) {
       logger.debug("loadTEICorpusVirtual");
        new VirtualEditionsTEICorpusImport().loadTEICorpusVirtual(new ByteArrayInputStream(inputStream));
    }

    @PostMapping("/loadTEIFragmentCorpus")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void loadTEIFragmentCorpus(@RequestBody Set<FragmentDto> fragments) {
       logger.debug("loadTEIFragmentCorpus");
        new GenerateTEIFragmentsCorpus().LoadFragmentCorpus(fragments);
    }

    @GetMapping("/virtualEdition/{acronym}/categoriesFromTaxonomy")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<CategoryDto> getCategoriesFromTaxonomy(@PathVariable("acronym") String acronym) {
       logger.debug("getCategoriesFromTaxonomy: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(virtualEdition -> virtualEdition.getTaxonomy().getCategoriesSet().stream()
                .map(CategoryDto::new)
                .collect(Collectors.toSet()))
                .orElse(new HashSet<>());
        //        return VirtualModule.getInstance().getVirtualEdition(acronym).getTaxonomy().getCategoriesSet().stream().map(CategoryDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/category/{externalId}/sortedInters")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<VirtualEditionInterDto> getSortedInterFromCategoriesTag(@PathVariable("externalId") String externalId) {
       logger.debug("getSortedInterFromCategoriesTag: " + externalId);
        Category category = FenixFramework.getDomainObject(externalId);
        if (category != null) {
            return category.getSortedInters().stream().map(VirtualEditionInterDto::new).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @GetMapping(value = "/category/{externalId}/sortedInters", params = "acronym")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<VirtualEditionInterDto> getSortedInterFromCategoriesTag(@PathVariable("externalId") String externalId, @RequestParam(name = "acronym", required = false) String acronym) {
       logger.debug("getSortedInterFromCategoriesTag: " + acronym);
        Category category = FenixFramework.getDomainObject(externalId);
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (category != null && virtualEdition != null) {
            return category.getSortedInters(virtualEdition).stream().map(VirtualEditionInterDto::new).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @GetMapping("/category/{externalId}/sortedUsers")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> getSortedUsersFromCategoriesTag(@PathVariable("externalId") String externalId) {
       logger.debug("getSortedUsersFromCategoriesTag: " + externalId );
        Category category = FenixFramework.getDomainObject(externalId);
        if (category != null) {
            return category.getSortedUsers();
        }
        return new ArrayList<>();
    }

    @GetMapping("/category/{externalId}/sortedEditions")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<VirtualEditionDto> getSortedEditionsFromCategoriesTag(@PathVariable("externalId") String externalId) {
       logger.debug("getSortedEditionsFromCategoriesTag: " + externalId);
        Category category = FenixFramework.getDomainObject(externalId);
        if (category != null) {
            return category.getSortedEditions().stream().map(VirtualEditionDto::new).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @GetMapping("/virtualEditionInter/{xmlId}/contributorSet")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<String> getContributorSetFromVirtualEditionInter(@RequestParam(name = "externalId") String externalId, @PathVariable("xmlId") String xmlId, @RequestParam(name = "username") String username) {
       logger.debug("getContributorSetFromVirtualEditionInter: " + xmlId);
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

    @GetMapping("/annotation/{externalId}/ranges")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<RangeJson> getRangeSetFromAnnotation(@PathVariable("externalId") String externalId) {
       logger.debug("getRangeSetFromAnnotation: " + externalId);
        Annotation annotation = FenixFramework.getDomainObject(externalId);
        if (annotation != null) {
            return annotation.getRangeSet().stream().map(RangeJson::new).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    @PostMapping("/createHumanAnnotation")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public HumanAnnotationDto createHumanAnnotation(@RequestParam(name = "xmlId") String xmlId, @RequestParam(name = "quote") String quote, @RequestParam(name = "text") String text, @RequestParam(name = "user") String user, @RequestBody List<RangeJson> ranges, @RequestParam(name = "tags") List<String> tags) {
      logger.debug("createHumanAnnotation: " + xmlId);
        VirtualEditionInter inter = VirtualModule.getInstance().getVirtualEditionInterByXmlId(xmlId);
        if (inter != null) {
            return new HumanAnnotationDto(inter.createHumanAnnotation(quote, text, user, ranges, tags), inter);
        }
        return null;
    }

    @GetMapping("/humanAnnotation/ext/{Id}")
    @Atomic(mode = Atomic.TxMode.READ)
    public HumanAnnotationDto getHumanAnnotationfromId(@PathVariable("Id") String Id) {
       logger.debug("getHumanAnnotationfromId: " + Id);
        HumanAnnotation annotation = FenixFramework.getDomainObject(Id);
        if (annotation != null) {
            return new HumanAnnotationDto(annotation, annotation.getVirtualEditionInter());
        }
        return null;
    }

    @GetMapping("/humanAnnotation/{Id}/canUserUpdate")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean canUserUpdateHumanAnnotation(@PathVariable("Id") String Id, @RequestParam(name = "user") String user) {
       logger.debug("canUserUpdateHumanAnnotation: " + user);
        HumanAnnotation annotation = FenixFramework.getDomainObject(Id);
        if (annotation != null) {
            return annotation.canUpdate(user);
        }
        return false;
    }

    @PostMapping("/humanAnnotation/{id}/update")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public HumanAnnotationDto updateHumanAnnotation(@PathVariable("id") String id, @RequestParam(name = "text") String text, @RequestParam(name = "tags") List<String> tags) {
       logger.debug("updateHumanAnnotation: " + id);
        HumanAnnotation annotation = FenixFramework.getDomainObject(id);
        if (annotation != null) {
            annotation.update(text, tags);
            return new HumanAnnotationDto(annotation, annotation.getVirtualEditionInter());
        }
        return null;
    }

    @GetMapping("/humanAnnotation/{id}/canUserDelete")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean canUserDeleteHumanAnnotation(@PathVariable("id") String id, @RequestParam(name = "user") String user) {
       logger.debug("canUserDeleteHumanAnnotation: " + id);
        HumanAnnotation annotation = FenixFramework.getDomainObject(id);
        if (annotation != null) {
            return annotation.canDelete(user);
        }
        return false;
    }

    @PostMapping("/removeHumanAnnotation")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeHumanAnnotation(@RequestParam(name = "id") String id) {
       logger.debug("removeHumanAnnotation: " + id);
        HumanAnnotation annotation = FenixFramework.getDomainObject(id);
        if (annotation != null) {
            annotation.remove();
        }
    }

    @GetMapping("/archiveVirtualEdition")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionDto getArchiveVirtualEdition() {
       logger.debug("getArchiveVirtualEdition");
        VirtualEdition archive = VirtualModule.getInstance().getArchiveEdition();
        if (archive != null) {
            return new VirtualEditionDto(VirtualModule.getInstance().getArchiveEdition());
        }
        return null;
    }

    @PostMapping("/virtualEdition/{xmlId}/edit")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void editVirtualEdition(@PathVariable("xmlId") String xmlId, @RequestParam(name = "acronym") String acronym, @RequestParam(name = "title") String title, @RequestParam(name = "synopsis") String synopsis, @RequestParam(name = "pub") boolean pub, @RequestParam(name = "management") boolean management, @RequestParam(name = "vocabulary") boolean vocabulary, @RequestParam(name = "annotation") boolean annotation, @RequestParam(name = "mediaSource") String mediaSource, @RequestParam(name = "beginDate") String beginDate, @RequestParam(name = "endDate") String endDate, @RequestParam(name = "geoLocation") String geoLocation, @RequestParam(name = "frequency") String frequency) {
      logger.debug("editVirtualEdition: " + xmlId);
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEditionByXmlId(xmlId);
        if (virtualEdition != null) {
            virtualEdition.edit(VirtualEdition.ACRONYM_PREFIX + acronym, title, synopsis, pub, management, vocabulary, annotation, mediaSource, beginDate, endDate, geoLocation, frequency);
        }
    }

    @PostMapping("/searchForAwareAnnotations")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void searchForAwareAnnotations(@RequestParam(name = "externalId") String externalId) {
       logger.debug("searchForAwareAnnotations: " + externalId);
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

    @PostMapping("/removeVirtualEdition")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeVirtualEditionByExternalId(@RequestParam(name = "externalId") String externalId) {
       logger.debug("removeVirtualEditionByExternalId: " + externalId);
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            virtualEditionMap.clear();
            virtualEditionInterMapByXmlId.clear();
            virtualEditionInterMapByUrlId.clear();
            ((VirtualEdition) virtualEdition).remove();
        }
    }

    @PostMapping("/virtualEdition/{externalId}/updateVirtualEditionInters")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void updateVirtualEditionInters(@PathVariable("externalId") String externalId, @RequestParam(name = "fragIntersXmlIds") List<String> fragIntersXmlIds) {
       logger.debug("updateVirtualEditionInters: " + externalId );
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            ((VirtualEdition) virtualEdition).updateVirtualEditionInters(fragIntersXmlIds);
        }
    }

    @PostMapping("/virtualEdition/{externalId}/addMember")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void addMemberByExternalId(@PathVariable("externalId") String externalId, @RequestParam(name = "user") String user, @RequestParam(name = "b") boolean b) {
       logger.debug("addMemberByExternalId: " + externalId + ", " + user);
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            ((VirtualEdition) virtualEdition).addMember(user, Member.MemberRole.MEMBER, b);
        }
    }

    @PostMapping("/virtualEdition/{externalId}/cancelParticipationSubmission")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void cancelParticipationSubmissionByExternalId(@PathVariable("externalId") String externalId, @RequestParam(name = "user") String user) {
       logger.debug("cancelParticipationSubmissionByExternalId: " + externalId);
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            ((VirtualEdition) virtualEdition).cancelParticipationSubmission(user);
        }
    }

    @PostMapping("/virtualEdition/{externalId}/addApprove")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void addApproveByExternalId(@PathVariable("externalId") String externalId, @RequestParam(name = "username") String username) {
       logger.debug("addApproveByExternalId: " + externalId);
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            ((VirtualEdition) virtualEdition).addApprove(username);
        }
    }

    @PostMapping("/virtualEdition/{externalId}/canSwitchRole")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public boolean canSwitchRole(@PathVariable("externalId") String externalId, @RequestParam(name = "authenticatedUser") String authenticatedUser, @RequestParam(name = "username") String username) {
       logger.debug("canSwitchRole: " + externalId);
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            return ((VirtualEdition) virtualEdition).canSwitchRole(authenticatedUser, username);
        }
       return false;
    }

    @PostMapping("/virtualEdition/{externalId}/switchRole")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void switchRole(@PathVariable("externalId") String externalId, @RequestParam(name = "username") String username) {
       logger.debug("switchRole: " + externalId);
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            ((VirtualEdition) virtualEdition).switchRole(username);
        }
    }

    @GetMapping("/virtualEdition/{externalId}/canRemoveMember")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean canRemoveMember(@PathVariable("externalId") String externalId, @RequestParam(name = "authenticatedUser") String authenticatedUser, @RequestParam(name = "user") String user) {
       logger.debug("canRemoveMember: " + externalId);
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            return ((VirtualEdition) virtualEdition).canRemoveMember(authenticatedUser, user);
        }
        return false;
    }


    @PostMapping("/virtualEdition/{externalId}/removeMember")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeMember(@PathVariable("externalId") String externalId, @RequestParam(name = "user") String user) {
       logger.debug("removeMember: " +externalId);
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            ((VirtualEdition) virtualEdition).removeMember(user);
        }
    }

    @PostMapping("/createVirtualEditionInterFromScholarInter")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public VirtualEditionInterDto createVirtualEditionInterFromScholarInter(@RequestParam(name = "externalId") String externalId, @RequestParam(name = "xmlId") String xmlId, @RequestParam(name = "max") int max) {
       logger.debug("createVirtualEditionInterFromScholarInter: " + externalId);
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        ScholarInterDto scholarInterDto = VirtualRequiresInterface.getInstance().getScholarInterByXmlId(xmlId);
        if (virtualEdition instanceof VirtualEdition && scholarInterDto != null) {
            return new VirtualEditionInterDto(((VirtualEdition) virtualEdition).createVirtualEditionInter(scholarInterDto, max));
        }
        return null;
    }

    @PostMapping("/createVirtualEditionInterFromVirtualEditionInter")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public VirtualEditionInterDto createVirtualEditionInterFromVirtualEditionInter(@RequestParam(name = "externalId") String externalId, @RequestParam(name = "interExternalId") String interExternalId, @RequestParam(name = "max") int max) {
       logger.debug("createVirtualEditionInterFromVirtualEditionInter: " + externalId);
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        DomainObject inter = FenixFramework.getDomainObject(interExternalId);
        if (virtualEdition instanceof VirtualEdition && inter instanceof VirtualEditionInter) {
            return new VirtualEditionInterDto(((VirtualEdition) virtualEdition).createVirtualEditionInter((VirtualEditionInter) inter , max));
        }
        return null;
    }

    @PostMapping("/taxonomy/{editionAcronym}/edit")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void editTaxonomy(@PathVariable("editionAcronym") String editionAcronym, @RequestParam(name = "management") boolean management, @RequestParam(name = "vocabulary") boolean vocabulary, @RequestParam(name = "annotation") boolean annotation) {
       logger.debug("editTaxonomy: " + editionAcronym);
        getVirtualEditionByAcronymUtil(editionAcronym).map(VirtualEdition_Base::getTaxonomy).ifPresent(taxonomy -> taxonomy.edit(management, vocabulary, annotation));
    }

    @PostMapping("/generateTopicModeler")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public TopicListDTO generateTopicModeler(@RequestParam(name = "username") String username, @RequestParam(name = "externalId") String externalId, @RequestParam(name = "numTopics") int numTopics, @RequestParam(name = "numWords") int numWords, @RequestParam(name = "thresholdCategories") int thresholdCategories, @RequestParam("numIterations") int numIterations) throws IOException {
       logger.debug("generateTopicModeler: " + username);
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            TopicModeler topicModeler = new TopicModeler();
            return topicModeler.generate(username, (VirtualEdition) virtualEdition, numTopics, numWords, thresholdCategories, numIterations);
        }
        return null;
    }

    @PostMapping("/createGeneratedCategories")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createGeneratedCategories(@RequestParam(name = "editionAcronym") String editionAcronym, @RequestBody TopicListDTO topicList) {
//        VirtualModule.getInstance().getVirtualEdition(editionAcronym).getTaxonomy().createGeneratedCategories(topicList);
      logger.debug("createGeneratedCategories: " + editionAcronym);
        getVirtualEditionByAcronymUtil(editionAcronym).map(VirtualEdition_Base::getTaxonomy).ifPresent(taxonomy -> taxonomy.createGeneratedCategories(topicList));
    }

    @GetMapping("/taxonomy/ext/{externalId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public TaxonomyDto getTaxonomyByExternalId(@PathVariable("externalId") String externalId) {
       logger.debug("getTaxonomyByExternalId: " + externalId);
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);
        if (domainObject instanceof Taxonomy) {
            return new TaxonomyDto((Taxonomy) domainObject);
        }
        return null;
    }

    @PostMapping("/removeTaxonomy")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeTaxonomy(@RequestParam(name = "editionAcronym") String editionAcronym) {
       logger.debug("removeTaxonomy: " + editionAcronym);
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(editionAcronym);
        if (virtualEdition != null) {
            virtualEdition.getTaxonomy().remove();
            virtualEdition.setTaxonomy(new Taxonomy());
        }
    }

    @PostMapping("/createCategory")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createCategory(@RequestParam(name = "editionAcronym") String editionAcronym, @RequestParam(name = "name") String name) {
       logger.debug("createCategory: " + editionAcronym);
        getVirtualEditionByAcronymUtil(editionAcronym).map(VirtualEdition_Base::getTaxonomy).ifPresent(taxonomy -> taxonomy.createCategory(name));
    }

    @GetMapping("/category/ext/{externalId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public CategoryDto getCategoryByExternalId(@PathVariable("externalId") String externalId) {
       logger.debug("getCategoryByExternalId: " + externalId);
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof Category) {
            return new CategoryDto((Category) domainObject);
        }

        return null;
    }

    @PostMapping("/category/{externalId}/updateName")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void updateCategoryNameByExternalId(@PathVariable("externalId") String externalId, @RequestParam(name = "name") String name) {
       logger.debug("updateCategoryNameByExternalId: " + externalId);
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);
        if (domainObject instanceof Category) {
            ((Category) domainObject).setName(name);
        }
    }

    @PostMapping("/category/{externalId}/remove")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeCategory(@PathVariable("externalId") String externalId) {
       logger.debug("removeCategory: " + externalId);
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof Category) {
            ((Category) domainObject).remove();
        }

    }

    @PostMapping("/mergeCategories")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public CategoryDto mergeCategories(@RequestParam(name = "editionAcronym") String editionAcronym, @RequestBody List<CategoryDto> categories) {
       logger.debug("mergeCategories: " + editionAcronym);
        List<Category> categoryList = categories.stream().map(categoryDto -> (Category) FenixFramework.getDomainObject(categoryDto.getExternalId())).collect(Collectors.toList());
        return new CategoryDto(VirtualModule.getInstance().getVirtualEdition(editionAcronym).getTaxonomy().merge(categoryList));
    }

    @PostMapping("/deleteCategories")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteCategories(@RequestParam(name = "editionAcronym") String editionAcronym, @RequestBody List<CategoryDto> categories) {
       logger.debug("deleteCategories: " + editionAcronym);
        List<Category> categoryList = categories.stream().map(categoryDto -> (Category) FenixFramework.getDomainObject(categoryDto.getExternalId())).collect(Collectors.toList());
        VirtualModule.getInstance().getVirtualEdition(editionAcronym).getTaxonomy().delete(categoryList);
    }

    @PostMapping("/extractCategories")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public CategoryDto extractCategories(@RequestParam(name = "editionAcronym") String editionAcronym, @RequestParam(name = "externalId") String externalId, @RequestBody String[] interIds) {
       logger.debug("extractCategories: " + editionAcronym);
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof Category) {
            Category category = ((Category) domainObject);
            Set<VirtualEditionInter> inters = Arrays.stream(interIds).map(s -> (VirtualEditionInter) FenixFramework.getDomainObject(s)).collect(Collectors.toSet());
            return new CategoryDto(VirtualModule.getInstance().getVirtualEdition(editionAcronym).getTaxonomy().extract(category, inters));
        }
        return null;

    }

    @GetMapping("/virtualEdition/{acronym}/mediaSource")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getMediaSourceName(@PathVariable("acronym") String acronym) {
       logger.debug("getMediaSourceName: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition:: getMediaSource).map(MediaSource::getName).orElse(null);

    }

    @GetMapping("/virtualEdition/{acronym}/timeWindowBeginDate")
    @Atomic(mode = Atomic.TxMode.READ)
    public LocalDate getTimeWindowBeginDate(@PathVariable("acronym") String acronym) {
       logger.debug("getTimeWindowBeginDate: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getTimeWindow).map(TimeWindow::getBeginDate).orElse(null);
    }

    @GetMapping("/virtualEdition/{acronym}/timeWindowEndDate")
    @Atomic(mode = Atomic.TxMode.READ)
    public LocalDate getTimeWindowEndDate(@PathVariable("acronym") String acronym) {
       logger.debug("getTimeWindowEndDate: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getTimeWindow).map(TimeWindow::getEndDate).orElse(null);
    }

    @GetMapping("/virtualEdition/{acronym}/containsEveryCountryinGeographicLocation")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean containsEveryCountryinGeographicLocation(@PathVariable("acronym") String acronym) {
       logger.debug("containsEveryCountryinGeographicLocation: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getGeographicLocation).map(GeographicLocation::containsEveryCountry).orElse(false);
    }

    @GetMapping("/virtualEdition/{acronym}/containsCountryinGeographicLocation")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean containsCountryinGeographicLocation(@PathVariable("acronym") String acronym, @RequestParam(name = "country") String country) {
       logger.debug("containsCountryinGeographicLocation: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getGeographicLocation)
                .map(geographicLocation -> geographicLocation.containsCountry(country)).orElse(false);
    }

    @GetMapping("/virtualEdition/{acronym}/integerFrequency")
    @Atomic(mode = Atomic.TxMode.READ)
    public int getIntegerFrequency(@PathVariable("acronym") String acronym) {
       logger.debug("getIntegerFrequency: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).map(VirtualEdition::getFrequency).map(Frequency_Base::getFrequency).orElse(0);
    }

    @GetMapping("/virtualEdition/{acronym}/activeMembers")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<MemberDto> getActiveMembersFromVirtualEdition(@PathVariable("acronym") String acronym) {
       logger.debug("getActiveMembersFromVirtualEdition: " + acronym);
        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (edition != null) {
            return edition.getActiveMemberSet().stream().map(MemberDto::new).collect(Collectors.toSet());
        }

        return new HashSet<>();
    }

    @GetMapping("/virtualEdition/{acronym}/pendingMember")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<MemberDto> getPendingMemberFromVirtualEdition(@PathVariable("acronym") String acronym) {
       logger.debug("getPendingMemberFromVirtualEdition: " + acronym);
        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (edition != null ) {
            return edition.getPendingMemberSet().stream().map(MemberDto::new).collect(Collectors.toSet());
        }

        return new HashSet<>();
    }

    @GetMapping("/taxonomy/{editionAcronym}/taxonomyUsedIn")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<VirtualEditionDto> getTaxonomyUsedIn(@PathVariable("editionAcronym") String editionAcronym) {
       logger.debug("getTaxonomyUsedIn: " + editionAcronym);
        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(editionAcronym);
        if (edition != null) {
            return edition.getTaxonomy().getUsedIn().stream().map(VirtualEditionDto::new).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @GetMapping("/writeVirtualEditionToFileExport")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getWriteVirtualEditionToFileExport() throws IOException {
       logger.debug("getWriteVirtualEditionToFileExport");
        WriteVirtualEditonsToFile write = new WriteVirtualEditonsToFile();
        return write.export();
    }

    @PostMapping("/importVirtualEditionCorpus")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void importVirtualEditionCorpus(@RequestBody byte[] inputStream) {
       logger.debug("importVirtualEditionCorpus");
        VirtualEditionsTEICorpusImport loader = new VirtualEditionsTEICorpusImport();
        loader.importVirtualEditionsCorpus(new ByteArrayInputStream(inputStream));
    }

    @PostMapping("/importVirtualEditionCorpusString")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void importVirtualEditionCorpus(@RequestBody String inputStream) {
       logger.debug("importVirtualEditionCorpus");
        VirtualEditionsTEICorpusImport loader = new VirtualEditionsTEICorpusImport();
        loader.importVirtualEditionsCorpus((inputStream));
    }

    @PostMapping("/importVirtualEditionFragmentFromTEI")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public String importVirtualEditionFragmentFromTEI(@RequestBody byte[] inputStream) {
       logger.debug("importVirtualEditionFragmentFromTEI");
        VirtualEditionFragmentsTEIImport loader = new VirtualEditionFragmentsTEIImport();
        return loader.importFragmentFromTEI(new ByteArrayInputStream(inputStream));
    }

    @PostMapping("/importVirtualEditionFragmentFromTEIString")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void importVirtualEditionFragmentFromTEI(@RequestBody String inputStream) {
       logger.debug("importVirtualEditionFragmentFromTEI");
        VirtualEditionFragmentsTEIImport loader = new VirtualEditionFragmentsTEIImport();
        loader.importFragmentFromTEI((inputStream));
    }

    @GetMapping("/allTwitterCitations")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public List<TwitterCitationDto> getAllTwitterCitations() {
       logger.debug("getAllTwitterCitations");
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        return  VirtualModule.getInstance().getAllTwitterCitation().stream()
                .sorted((c1, c2) -> java.time.LocalDateTime.parse(c2.getDate(), formater)
                        .compareTo(java.time.LocalDateTime.parse(c1.getDate(), formater)))
                .map(TwitterCitationDto::new).collect(Collectors.toList());
    }

    @GetMapping("/allTweets")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<TweetDto> getAllTweets() {
       logger.debug("getAllTweets");
        return VirtualModule.getInstance().getTweetSet().stream().map(TweetDto::new).collect(Collectors.toSet());
    }

    @PostMapping("/removeTweets")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeTweets() {
       logger.debug("removeTweets");
        VirtualModule.getInstance().removeTweets();
    }

    @PostMapping("/detectCitation")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void detectCitation() throws IOException {
       logger.debug("detectCitation");
        CitationDetecter detecter = new CitationDetecter();
        detecter.detect();
    }

    @PostMapping("/createTweetFactory")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createTweetFactory() throws IOException {
       logger.debug("createTweetFactory");
        TweetFactory tweetFactory = new TweetFactory();
        tweetFactory.create();
    }

    @PostMapping("/generateAwareAnnotations")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void generateAwareAnnotations() throws IOException {
       logger.debug("generateAwareAnnotations");
        AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();
        awareFactory.generate();
    }

    @PostMapping("/dailyRegenerateTwitterCitationEdition")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void dailyRegenerateTwitterCitationEdition() {
       logger.debug("dailyRegenerateTwitterCitationEdition");
        VirtualModule.dailyRegenerateTwitterCitationEdition();
    }

    @GetMapping("/virtualEdition/{acronym}/annotationTextList")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> getAnnotationTextListFromVirtualEdition(@PathVariable("acronym") String acronym) {
       logger.debug("getAnnotationTextListFromVirtualEdition: " + acronym);
        VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (edition != null && !edition.getAnnotationTextList().isEmpty()) {
            return edition.getAnnotationTextList();
        }
        return new ArrayList<>();
    }

    @GetMapping("/virtualEditionInter/{xmlId}/allDepthCategoriesAccessibleByUser")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<CategoryDTO> getVirtualEditionInterAllDepthCategoriesAccessibleByUser(@PathVariable("xmlId") String xmlId, @RequestParam(name = "username") String username) {
       logger.debug("getVirtualEditionInterAllDepthCategoriesAccessibleByUser: " + xmlId);
        VirtualEditionInter inter = getVirtualEditionInterByXmlId(xmlId).orElseThrow(LdoDException::new);
        return inter.getAllDepthCategoriesAccessibleByUser(username).stream()
                .sorted((c1, c2) -> c1.compareInEditionContext(inter.getVirtualEdition(), c2))
                .map(c -> new CategoryDTO(inter.getVirtualEdition(), c)).collect(Collectors.toList());
    }

    @GetMapping("/virtualEdition/{acronym}/canCreateHumanAnnotation")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean canCreateHumanAnnotationOnVirtualEdition(@PathVariable("acronym") String acronym, @RequestParam(name = "username") String username) {
       logger.debug("canCreateHumanAnnotationOnVirtualEdition: " + acronym);
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (virtualEdition != null) {
            return HumanAnnotation.canCreate(virtualEdition, username);
        }
        return false;
    }

    @GetMapping("/virtualEdition/{acronym}/fragInter")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionInterDto getVirtualEditionFragInterByUrlId(@PathVariable("acronym") String acronym, @RequestParam(name = "urlId") String urlId) {
       logger.debug("getVirtualEditionFragInterByUrlId: " + acronym);
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (virtualEdition != null) {
            return virtualEdition.getIntersSet().stream().filter(i -> i.getUrlId().equals(urlId)).findFirst().map(VirtualEditionInterDto::new).orElse(null);
        }
        return null;
    }

    @GetMapping("/virtualEdition/{acronym}/interList")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionInterListDto getVirtualEditionInterList(@PathVariable("acronym") String acronym, @RequestParam(name = "deep") boolean deep) {
       logger.debug("getVirtualEditionInterList: " + acronym);
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (virtualEdition != null) {
            return new VirtualEditionInterListDto(virtualEdition, deep);
        }
        return null;
    }

    @GetMapping("/virtualEditionsUserIsParticipantSelectedOrPublic")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<VirtualEditionDto> getVirtualEditionsUserIsParticipantSelectedOrPublic(@RequestParam(name = "username") String username) {
       logger.debug("getVirtualEditionsUserIsParticipantSelectedOrPublic: " + username);
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

    @PostMapping("/initializeVirtualModule")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public boolean initializeVirtualModule() {
       logger.debug("initializeVirtualModule");
        return VirtualBootstrap.initializeVirtualModule();
    }

    @PostMapping("/fetchCitationsFromTwitter")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void fetchCitationsFromTwitter() throws IOException {
       logger.debug("fetchCitationsFromTwitter");
        FetchCitationsFromTwitter fetch = new FetchCitationsFromTwitter();
        fetch.fetch();
    }

    // Test methods

    @PostMapping("/createTwitterCitation")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public TwitterCitationDto createTwitterCitation(@RequestParam(name = "fragmentXmlId") String fragmentXmlId, @RequestParam(name = "sourceLink") String sourceLink, @RequestParam(name = "date") String date, @RequestParam(name = "fragText") String fragText, @RequestParam(name = "tweetText") String tweetText,
                                      @RequestParam(name = "tweetID") long tweetID, @RequestParam(name = "location") String location, @RequestParam(name = "country") String country, @RequestParam(name = "username") String username, @RequestParam(name = "profURL") String profURL, @RequestParam(name = "profImgURL") String profImgURL) {
        logger.debug("createTwitterCitation");
        return new TwitterCitationDto(new TwitterCitation(VirtualRequiresInterface.getInstance().getFragmentByXmlId(fragmentXmlId), sourceLink, date, fragText,
                tweetText, tweetID, location, country, username, profURL, profImgURL));
    }

    @PostMapping("/createTweet")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createTweet(@RequestParam(name = "sourceLink") String sourceLink, @RequestParam(name = "date") String date, @RequestParam(name = "tweetText") String tweetText, @RequestParam(name = "tweetID") long tweetID, @RequestParam(name = "location") String location,
                           @RequestParam(name = "country") String country, @RequestParam(name = "username") String username, @RequestParam(name = "profURL") String profURL, @RequestParam(name = "profImgURL") String profImgURL, @RequestParam(name = "originalTweetID") long originalTweetID, @RequestParam(name = "isRetweet") boolean isRetweet) {
       logger.debug("createTweet");
        TwitterCitation twitterCitation = VirtualModule.getInstance().getTwitterCitationByTweetID(23);
        new Tweet(VirtualModule.getInstance(), sourceLink, date, tweetText, tweetID, location, country, username, profURL, profImgURL, originalTweetID, isRetweet, twitterCitation);
    }

    @GetMapping("/virtualEditionInter/{xmlId}/allDepthHumanAnnotationsAccessibleByUser")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<HumanAnnotationDto> getAllDepthHumanAnnotationsAccessibleByUser(@PathVariable("xmlId") String xmlId, @RequestParam(name = "username") String username) {
        VirtualEditionInter inter = VirtualModule.getInstance().getVirtualEditionInterByXmlId(xmlId);
        return inter.getAllDepthHumanAnnotationsAccessibleByUser(username).stream().map(humanAnnotation -> new HumanAnnotationDto(humanAnnotation, inter)).collect(Collectors.toList());
    }

    @PostMapping("/removeAnnotation")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeAnnotation(@RequestParam(name = "externalId") String externalId) {
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof Annotation) {
            ((Annotation) domainObject).remove();
        }
    }

//    @GetMapping("/virtualEditionInter/xmlId/{xmlId}")
//    @Atomic(mode = Atomic.TxMode.READ)
//    public VirtualEditionInterDto getVirtualEditionInterDtoByXmlId(@PathVariable("xmlId") String xmlId) {
//        return new VirtualEditionInterDto(getVirtualEditionInterByXmlId(xmlId).get());
//    }

    @GetMapping("/virtualEdition/{acronym}/members")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<MemberDto> getMemberSet(@PathVariable("acronym") String acronym) {
       logger.debug("getMemberSet: " + acronym);
        return getVirtualEditionByAcronymUtil(acronym).get().getMemberSet().stream().map(MemberDto::new).collect(Collectors.toSet());
    }

    @PostMapping("/virtualEdition/{externalId}/addMemberAdmin")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void addMemberAdminByExternalId(@PathVariable("externalId") String externalId, @RequestParam(name = "user") String user, @RequestParam(name = "b") boolean b) {
        DomainObject virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition instanceof VirtualEdition) {
            ((VirtualEdition) virtualEdition).addMember(user, Member.MemberRole.ADMIN, b);
        }
    }

    @PostMapping("/createTestCategory")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public CategoryDto createTestCategory(@RequestParam(name = "editionAcronym") String editionAcronym, @RequestParam(name = "name") String name) {
        return new CategoryDto(getVirtualEditionByAcronymUtil(editionAcronym).get().getTaxonomy().createCategory(name));
    }

    @PostMapping("/virtualEditionInter/{externalId}/remove")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeVirtualEditionInter(@PathVariable("externalId") String externalId) {
        DomainObject inter = FenixFramework.getDomainObject(externalId);
        if (inter instanceof VirtualEditionInter) {
            cleanVirtualEditionMapCache();
            virtualEditionInterMapByXmlId.clear();
            virtualEditionInterMapByUrlId.clear();
            ((VirtualEditionInter) inter).remove();
        }
    }

    @GetMapping("/humanAnnotation/{id}/virtualEditionInter")
    @Atomic(mode = Atomic.TxMode.READ)
    public VirtualEditionInterDto getVirtualEditionInterFromAnnotation(@PathVariable("id") String id) {
        DomainObject domainObject = FenixFramework.getDomainObject(id);
        if (domainObject instanceof HumanAnnotation) {
            return new VirtualEditionInterDto((((HumanAnnotation) domainObject).getVirtualEditionInter()));
        }
        return null;
    }

    @GetMapping("/exportVirtualEditionFragments")
    @Atomic(mode = Atomic.TxMode.READ)
    public String exportVirtualEditionFragments(@RequestParam(name = "fragXmlId") String fragXmlId) {
        VirtualEditionFragmentsTEIExport export = new VirtualEditionFragmentsTEIExport();
        return export.exportFragment(fragXmlId);
    }

    @GetMapping("/virtualEditionIntersFromFragment")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<VirtualEditionInterDto> getVirtualEditionInterSetFromFragment(@RequestParam(name = "fragXmlId") String xmlId) {
        return VirtualModule.getInstance().getVirtualEditionInterSet(xmlId).stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet());
    }

    @PostMapping("/createTwitterCitationFromCitation")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public TwitterCitationDto createTwitterCitationFromCitation(@RequestBody CitationDto citationDto) {
       logger.debug("createTwitterCitationFromCitation: " + citationDto.getId());
        return new TwitterCitationDto(new TwitterCitation(citationDto));
    }

    @GetMapping("/twitterCitation/{id}/awareAnnotations")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public Set<AwareAnnotationDto> getAwareAnnotations(@PathVariable("id") long id) {
       logger.debug("getAwareAnnotations: " + id);
        return VirtualModule.getInstance().getTwitterCitationByTweetID(id).getAwareAnnotationSet().stream().map(AwareAnnotationDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/exportVirtualEditionsTEICorpus")
    @Atomic(mode = Atomic.TxMode.READ)
    public String exportVirtualEditionsTEICorpus() {
        return new VirtualEditionsTEICorpusExport().export();
    }

    @PostMapping("/removeTweet")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeTweet(@RequestParam(name = "id") long id) {
        VirtualModule.getInstance().getTweetByTweetID(id).remove();
    }

    @GetMapping("/virtualEdition/{acronym}/criteriaSize")
    @Atomic(mode = Atomic.TxMode.READ)
    public int getVirtualEditionCriteriaSetSize(@PathVariable("acronym") String acronym) {
        return VirtualModule.getInstance().getVirtualEdition(acronym).getCriteriaSet().size();
    }

    @PostMapping("/virtualEdition/{acronym}/createMediaSource")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createMediaSource(@PathVariable("acronym") String acronym, @RequestParam(name = "name") String name) {
        new MediaSource(VirtualModule.getInstance().getVirtualEdition(acronym), name );
    }

    @PostMapping("/virtualEdition/{acronym}/createTimeWindow")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createTimeWindow(@PathVariable("acronym") String acronym, @RequestParam(name = "beginDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginDate,  @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        new TimeWindow(VirtualModule.getInstance().getVirtualEdition(acronym), beginDate, endDate);
    }

    @PostMapping("/virtualEdition/{acronym}/createFrequency")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createFrequency(@PathVariable("acronym") String acronym, @RequestParam(name = "frequency") int frequency) {
        new Frequency(VirtualModule.getInstance().getVirtualEdition(acronym), frequency);
    }

    @PostMapping("/virtualEdition/{acronym}/createGeographicLocation")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createGeographicLocation(@PathVariable("acronym") String acronym, @RequestParam(name = "country") String country, @RequestParam(name = "location") String location) {
        new GeographicLocation(VirtualModule.getInstance().getVirtualEdition(acronym), country, location);
    }

    @GetMapping("/citationDetecterLastIndexOfCapitalLetter")
    @Atomic(mode = Atomic.TxMode.READ)
    public int citationDetecterLastIndexOfCapitalLetter(@RequestParam(name = "teste") String teste, @RequestParam(name = "i") int i) throws IOException {
        CitationDetecter citationDetecter = new CitationDetecter();
        return citationDetecter.lastIndexOfCapitalLetter(teste, i);
    }

    @GetMapping("/citationDetecterPatternFinding")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> citationDetecterPatternFinding(@RequestParam(name = "text") String text, @RequestParam(name = "pattern") String pattern) throws IOException {
        CitationDetecter citationDetecter = new CitationDetecter();
        return citationDetecter.patternFinding(text, pattern);
    }

    @GetMapping("/citationDetecterMaxJaroValue")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> citationDetecterMaxJaroValue(@RequestParam(name = "text") String text, @RequestParam(name = "wordToFind") String wordToFind) throws IOException {
        CitationDetecter citationDetecter = new CitationDetecter();
        return citationDetecter.maxJaroValue(text, wordToFind);
    }

    @GetMapping("/citationDetecterCleanTweetText")
    @Atomic(mode = Atomic.TxMode.READ)
    public String citationDetecterCleanTweetText(@RequestParam(name = "s") String s) throws IOException {
        CitationDetecter citationDetecter = new CitationDetecter();
        return citationDetecter.cleanTweetText(s);
    }

    @GetMapping("/citationDetecterCountOccurencesOfSubstring")
    @Atomic(mode = Atomic.TxMode.READ)
    public int countOccurencesOfSubstring(@RequestParam(name = "string") String string, @RequestParam(name = "substring") String substring, @RequestParam(name = "position") int position) throws IOException {
        CitationDetecter citationDetecter = new CitationDetecter();
        return citationDetecter.countOccurencesOfSubstring(string, substring, position);
    }

    @GetMapping("/citationDetecterStartBiggerThanEnd")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean startBiggerThanEnd(@RequestParam(name = "hStart") int hStart, @RequestParam(name = "hEnd") int hEnd, @RequestParam(name = "nStart") int nStart, @RequestParam(name = "nEnd") int nEnd ) throws IOException {
        CitationDetecter citationDetecter = new CitationDetecter();
        return citationDetecter.startBiggerThanEnd(hStart, hEnd, nStart, nEnd);
    }

    @GetMapping("/checkTitleStmtLoad")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> checkTitleStmtLoad() {
        List<String> list = new ArrayList<>();
        list.add(VirtualModule.getInstance().getTitle());
        list.add(VirtualModule.getInstance().getAuthor());
        list.add(VirtualModule.getInstance().getEditor());
        list.add(VirtualModule.getInstance().getSponsor());
        list.add(VirtualModule.getInstance().getFunder());
        list.add(VirtualModule.getInstance().getPrincipal());
        return list;
    }

    @GetMapping("/twitterCitation/{tweetId}/numberOfRetweets")
    @Atomic(mode = Atomic.TxMode.READ)
    public int getTwitterCitationNumberOfRetweets(@PathVariable("tweetId") long tweetId) {
       return VirtualModule.getInstance().getTwitterCitationByTweetID(tweetId).getNumberOfRetweets();
    }

    @GetMapping("/twitterCitation/{tweetId}/tweets")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<TweetDto> getTwitterCitationTweets(@PathVariable("tweetId") long tweetId) {
        return VirtualModule.getInstance().getTwitterCitationByTweetID(tweetId).getTweetSet().stream().map(TweetDto::new).collect(Collectors.toSet());
    }

    @PostMapping("/removeVirtualModule")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeVirtualModule() {
        VirtualModule virtualModule = VirtualModule.getInstance();
        if (virtualModule != null) {
            virtualModule.remove();
        }
    }
}




