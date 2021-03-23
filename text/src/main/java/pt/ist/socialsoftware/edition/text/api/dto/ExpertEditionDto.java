package pt.ist.socialsoftware.edition.text.api.dto;

import pt.ist.socialsoftware.edition.text.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;

import java.util.List;

public class ExpertEditionDto {
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    private final String acronym;

    // cached attributes
    private String editor;
    private String externalId;

    public ExpertEditionDto(ExpertEdition expertEdition) {
        this.acronym = expertEdition.getAcronym();
        this.editor = expertEdition.getEditor();
        this.externalId = expertEdition.getExternalId();
    }

    public String getAcronym() {
        return this.acronym;
    }

    public String getEditor() {
        //return this.textProvidesInterface.getExpertEditionEditorByEditionAcronym(this.acronym);
        return this.editor;
    }

    public String getExternalId() {
        return externalId;
    }

    public List<ScholarInterDto> getExpertEditionInters() {
        return this.textProvidesInterface.getExpertEditionScholarInterDtoList(this.acronym);
    }

    public List<ScholarInterDto> getSortedInter4Frag(String fragmentXmlId) {
        return this.textProvidesInterface.getExpertEditionSortedInter4Frag(this.acronym, fragmentXmlId);
    }

    public boolean isExpertEdition() {
        return true;
    }

    public boolean isVirtualEdition() {
        return false;
    }

}
