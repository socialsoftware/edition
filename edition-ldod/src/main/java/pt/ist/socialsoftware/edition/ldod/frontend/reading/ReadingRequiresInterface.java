package pt.ist.socialsoftware.edition.ldod.frontend.reading;

import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;

import java.util.List;

public class ReadingRequiresInterface {
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public List<ExpertEditionDto> getSortedExpertEditionsDto() {
        return this.textProvidesInterface.getSortedExpertEditionsDto();
    }

    public FragmentDto getFragmentByXmlId(String xmlId) {
        return this.textProvidesInterface.getFragmentByXmlId(xmlId);
    }

    public ScholarInterDto getExpertEditionFirstInterpretation(String acronym) {
        return this.textProvidesInterface.getExpertEditionFirstInterpretation(acronym);
    }

    public ScholarInterDto getScholarInterbyExternalId(String expertEditionInterId) {
        return this.textProvidesInterface.getScholarInterbyExternalId(expertEditionInterId);
    }
}
