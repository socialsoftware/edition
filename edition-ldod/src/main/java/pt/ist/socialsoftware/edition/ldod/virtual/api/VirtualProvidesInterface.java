package pt.ist.socialsoftware.edition.ldod.virtual.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
import pt.ist.socialsoftware.edition.ldod.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.dto.WeightsDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;
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

    public String getVirtualEditionInterShortName(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getShortName).orElse(null);
    }

    public ScholarInterDto getVirtualEditionLastUsedScholarInter(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getLastUsed).orElse(null);
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

    private Optional<VirtualEditionInter> getVirtualEditionInterByXmlId(String xmlId) {
        return VirtualModule.getInstance().getVirtualEditionInterSet().stream()
                .filter(virtualEditionInter -> virtualEditionInter.getXmlId().equals(xmlId)).findAny();
    }

    private Optional<VirtualEdition> getVirtualEditionByAcronym(String acronym) {
        return VirtualModule.getInstance().getVirtualEditionsSet().stream()
                .filter(virtualEdition -> virtualEdition.getAcronym().equals(acronym)).findAny();
    }


}
