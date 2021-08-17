package pt.ist.socialsoftware.edition.text.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.text.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;

import java.util.List;

public class ExpertEditionDto {

    private final String acronym;

    // cached attributes
    private String editor;
    private String externalId;
    private String author;
    private String title;

    public ExpertEditionDto(ExpertEdition expertEdition) {
        this.acronym = expertEdition.getAcronym();
        this.editor = expertEdition.getEditor();
        this.externalId = expertEdition.getExternalId();
        this.author = expertEdition.getAuthor();
        this.title = expertEdition.getTitle();
    }

    public String getAcronym() {
        return this.acronym;
    }

    public String getEditor() {
        //return this.textProvidesInterface.getExpertEditionEditorByEditionAcronym(this.acronym);
        return this.editor;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getExternalId() {
        return externalId;
    }

    public boolean isExpertEdition() {
        return true;
    }

    public boolean isVirtualEdition() {
        return false;
    }

}
