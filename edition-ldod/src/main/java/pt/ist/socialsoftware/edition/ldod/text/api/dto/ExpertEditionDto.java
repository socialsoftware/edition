package pt.ist.socialsoftware.edition.ldod.text.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;

public class ExpertEditionDto {
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    private final String acronym;

    public ExpertEditionDto(ExpertEdition expertEdition) {
        this.acronym = expertEdition.getAcronym();
    }

    public String getAcronym() {
        return this.acronym;
    }

    public String getEditor() {
        return this.textProvidesInterface.getExpertEditionEditorByEditionAcronym(this.acronym);
    }


}
