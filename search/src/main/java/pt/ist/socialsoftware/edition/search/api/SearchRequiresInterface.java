package pt.ist.socialsoftware.edition.search.api;

import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.text.api.dto.LdoDDateDto;
import pt.ist.socialsoftware.edition.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.text.api.dto.SourceDto;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionInterDto;


import java.util.List;
import java.util.Set;

public class SearchRequiresInterface {
    // Requires from Text Module
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

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

    public List<ScholarInterDto> searchScholarInterForWords(String words) {
        return this.textProvidesInterface.searchScholarInterForWords(words);
    }

    public Set<FragmentDto> getFragmentDtoSet() {
        return this.textProvidesInterface.getFragmentDtoSet();
    }


    // Requires from Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public Set<VirtualEditionInterDto> getVirtualEditionInterSet() {
        return this.virtualProvidesInterface.getVirtualEditionInterSet();
    }

    public List<String> getVirtualEditionSortedCategoryList(String xmlId) {
        return this.virtualProvidesInterface.getVirtualEditionSortedCategoryList(xmlId);
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