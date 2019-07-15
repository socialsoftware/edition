package pt.ist.socialsoftware.edition.ldod.visual.api;

import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionInterListDto;

import java.util.List;

public class VisualRequiresInterface {
    // Requires the Text Module
    TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public List<EditionInterListDto> getEditionInterListDto() {
        return this.textProvidesInterface.getEditionInterListDto();
    }

    public ExpertEditionDto getExpertEditionDto(String acronym) {
        return this.textProvidesInterface.getExpertEditionDto(acronym);
    }

    public List<ScholarInterDto> getExpertEditionScholarInterDtoList(String acronym) {
        return this.textProvidesInterface.getExpertEditionScholarInterDtoList(acronym);
    }

    // Requires the Virtual Module
    VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public List<EditionInterListDto> getPublicVirtualEditionInterListDto() {
        return this.virtualProvidesInterface.getPublicVirtualEditionInterListDto();
    }

    public VirtualEditionDto getVirtualEdition(String acronym) {
        return this.virtualProvidesInterface.getVirtualEdition(acronym);
    }
}
