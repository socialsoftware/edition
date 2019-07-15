package pt.ist.socialsoftware.edition.ldod.search.api;

import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.*;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

import java.util.List;
import java.util.Set;

public class SearchRequiresInterface {
    // Requires from Text Module
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public Set<FragmentDto> getFragmentDtoSet() {
        return this.textProvidesInterface.getFragmentDtoSet();
    }

    public boolean isSourceInter(String xmlId) {
        return !this.textProvidesInterface.isExpertInter(xmlId);
    }

    public SourceDto getSourceOfSourceInter(String xmlId) {
        return this.textProvidesInterface.getSourceOfSourceInter(xmlId);
    }

    public String getSourceInterType(String xmlId) {
        return this.textProvidesInterface.getSourceInterType(xmlId);
    }

    public LdoDDateDto getScholarInterDate(String xmlId) {
        return this.textProvidesInterface.getScholarInterDate(xmlId);
    }

    public boolean isExpertInter(String xmlId) {
        return this.textProvidesInterface.isExpertInter(xmlId);
    }

    public String getEditionAcronymOfInter(String xmlId) {
        return this.textProvidesInterface.getEditionAcronymOfInter(xmlId);
    }

    public String getHeteronymXmlId(String xmlId) {
        return this.textProvidesInterface.getHeteronymXmlId(xmlId);
    }

    public Set<HeteronymDto> getHeteronymDtoSet() {
        return this.textProvidesInterface.getHeteronymDtoSet();
    }

    public List<ScholarInterDto> searchScholarInterForWords(String words) {
        return this.textProvidesInterface.searchScholarInterForWords(words);
    }

    public String getFragmentTitle(String xmlId) {
        return this.textProvidesInterface.getFragmentTitle(xmlId);
    }

    public List<ExpertEditionDto> getSortedExpertEditionsDto() {
        return this.textProvidesInterface.getSortedExpertEditionsDto();
    }

    public List<ScholarInterDto> getExpertEditionScholarInterDtoList(String acronym) {
        return this.textProvidesInterface.getExpertEditionScholarInterDtoList(acronym);
    }


    // Requires from User Module
    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    public String getAuthenticatedUser() {
        return this.userProvidesInterface.getAuthenticatedUser();
    }


    // Requires from Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public Set<VirtualEditionInterDto> getVirtualEditionInterSet() {
        return this.virtualProvidesInterface.getVirtualEditionInterSet();
    }

    public Set<String> getTagsForVirtualEditionInter(String xmlId) {
        return this.virtualProvidesInterface.getTagsForVirtualEditionInter(xmlId);
    }

    public boolean isInterInVirtualEdition(String interXmlId, String virtualEditionAcronym) {
        return this.virtualProvidesInterface.isInterInVirtualEdition(interXmlId, virtualEditionAcronym);
    }

    public String getVirtualEditionAcronymByVirtualEditionInterXmlId(String interXmlId) {
        return this.virtualProvidesInterface.getVirtualEditionAcronymByVirtualEditionInterXmlId(interXmlId);
    }

    public Set<VirtualEditionDto> getPublicVirtualEditionsOrUserIsParticipant(String username) {
        return this.virtualProvidesInterface.getPublicVirtualEditionsOrUserIsParticipant(username);
    }

}