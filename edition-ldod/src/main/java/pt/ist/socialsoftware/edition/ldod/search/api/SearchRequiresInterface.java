package pt.ist.socialsoftware.edition.ldod.search.api;

import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.LdoDDateDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.SourceDto;
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
        return this.textProvidesInterface.isSourceInter(xmlId);
    }

    public boolean getSourceType(String xmlId, Source.SourceType manuscript) {
        return this.textProvidesInterface.getSourceType(xmlId, manuscript);
    }

    public SourceDto getSourceOfInter(String xmlId) {
        return this.textProvidesInterface.getSourceOfInter(xmlId);
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

    public List<ScholarInterDto> searchScholarInterForWords(String words) {
        return this.textProvidesInterface.searchScholarInterForWords(words);
    }

    public String getFragmentTitle(String xmlId) {
        return this.textProvidesInterface.getFragmentTitle(xmlId);
    }


    // Requires from Virtual Module
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