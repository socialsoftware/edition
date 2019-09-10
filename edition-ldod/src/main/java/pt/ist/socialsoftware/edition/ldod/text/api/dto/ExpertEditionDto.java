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

    private List<ScholarInterDto> expertInters;

    private Map<String, List<ScholarInterDto>> inter4Frag = new ConcurrentHashMap<>();

    public ExpertEditionDto(ExpertEdition expertEdition) {
        this.acronym = expertEdition.getAcronym();
    }

    public String getAcronym() {
        return this.acronym;
    }

    public String getEditor() {
        if(editor == null)
            editor = this.textProvidesInterface.getExpertEditionEditorByEditionAcronym(this.acronym);
        return editor;
    }

    public List<ScholarInterDto> getExpertEditionInters() {
        if(expertInters == null)
            expertInters = this.textProvidesInterface.getExpertEditionScholarInterDtoList(this.acronym);
        return expertInters;
    }

    public List<ScholarInterDto> getSortedInter4Frag(String fragmentXmlId) {
        if(!inter4Frag.containsKey(fragmentXmlId))
            inter4Frag.put(fragmentXmlId, this.textProvidesInterface.getExpertEditionSortedInter4Frag(this.acronym, fragmentXmlId));
        return inter4Frag.get(fragmentXmlId);
    }

    public boolean isExpertEdition() {
        return true;
    }

    public boolean isVirtualEdition() {
        return false;
    }

}
