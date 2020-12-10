package pt.ist.socialsoftware.edition.ldod.frontend.text;

import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

import java.util.List;
import java.util.Set;

public class FeTextRequiresInterface {
    // Uses Frontend User Module
    private final FeUserProvidesInterface feUserProvidesInterface = new FeUserProvidesInterface();

    public String getAuthenticatedUser() {
        return this.feUserProvidesInterface.getAuthenticatedUser();
    }


    // Uses Text Module
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public FragmentDto getFragmentByXmlId(String xmlId) {
        return this.textProvidesInterface.getFragmentByXmlId(xmlId);
    }

    public FragmentDto getFragmentByExternalId(String externalId) {
        return this.textProvidesInterface.getFragmentByExternalId(externalId);
    }

    public ScholarInterDto getScholarInterByExternalId(String externalId) {
        return this.textProvidesInterface.getScholarInterbyExternalId(externalId);
    }

    public List<ExpertEditionDto> getSortedExpertEditionsDto() {
        return this.textProvidesInterface.getSortedExpertEditionsDto();
    }

    public ExpertEditionDto getExpertEditionByAcronym(String acronym) {
        return this.textProvidesInterface.getExpertEditionDto(acronym);
    }

    public Set<FragmentDto> getFragmentDtoSet() {
        return this.textProvidesInterface.getFragmentDtoSet();
    }

    public Set<FragmentDto> getFragmentDtosWithSourceDtos() {
        return this.textProvidesInterface.getFragmentDtosWithSourceDtos();
    }


    // Uses Virtual Edition Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        return this.virtualProvidesInterface.getVirtualEditionByAcronym(acronym);
    }

    public VirtualEditionInterDto getVirtualEditionInterByUrlId(String urlId) {
        return this.virtualProvidesInterface.getVirtualEditionInterByUrlId(urlId);
    }

    public VirtualEditionInterDto getVirtualEditionInterByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
    }

}