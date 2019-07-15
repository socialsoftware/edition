package pt.ist.socialsoftware.edition.ldod.virtual.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
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

    private Optional<VirtualEditionInter> getVirtualEditionInterByXmlId(String xmlId) {
        return VirtualModule.getInstance().getVirtualEditionInterSet().stream()
                .filter(virtualEditionInter -> virtualEditionInter.getXmlId().equals(xmlId)).findAny();
    }

    private Optional<VirtualEdition> getVirtualEditionByAcronym(String acronym) {
        return VirtualModule.getInstance().getVirtualEditionsSet().stream()
                .filter(virtualEdition -> virtualEdition.getAcronym().equals(acronym)).findAny();
    }

}
