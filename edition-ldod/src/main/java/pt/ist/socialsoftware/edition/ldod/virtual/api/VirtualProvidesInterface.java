package pt.ist.socialsoftware.edition.ldod.virtual.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter_Base;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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

    public String getFragmentXmlIdVirtualEditionInter(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getFragmentXmlId).orElse(null);
    }

    public String getVirtualEditionInterUrlId(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getUrlId).orElse(null);
    }

    public String getVirtualEditionInterShortName(String xmlId) {
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getShortName).orElse(null);
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

    public int getVirtualEditionInterNumber(String xmlId){
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter::getNumber).orElse(null);
    }

    public String getVirtualEditionInterExternalId(String xmlId){
        return getVirtualEditionInterByXmlId(xmlId).map(virtualEditionInter -> virtualEditionInter.getExternalId())
                .orElse(null);
    }

    public VirtualEditionInterDto getNextVirtualInter(String xmlId){
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter.class::cast)
                .map(VirtualEditionInter::getNextNumberInter).map(VirtualEditionInterDto::new).orElse(null);
    }

    public VirtualEditionInterDto getPrevVirtualInter(String xmlId){
        return getVirtualEditionInterByXmlId(xmlId).map(VirtualEditionInter.class::cast)
                .map(VirtualEditionInter::getPrevNumberInter).map(VirtualEditionInterDto::new).orElse(null);
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
