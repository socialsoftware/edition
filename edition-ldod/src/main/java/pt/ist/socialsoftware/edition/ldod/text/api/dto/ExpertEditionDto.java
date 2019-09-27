package pt.ist.socialsoftware.edition.ldod.text.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpertEditionDto {
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    private final String acronym;

    // cached attributes
    private String editor;

    public ExpertEditionDto(ExpertEdition expertEdition) {
        this.acronym = expertEdition.getAcronym();
        this.editor = expertEdition.getEditor();
    }

    public String getAcronym() {
        return this.acronym;
    }

    public String getEditor() {
        //return this.textProvidesInterface.getExpertEditionEditorByEditionAcronym(this.acronym);
        return this.editor;
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
