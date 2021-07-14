package pt.ist.socialsoftware.edition.search.api.textDto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class ExpertEditionDto {

    private final String acronym;

    // cached attributes
    private String editor;
    private String externalId;
    private String author;
    private String title;



    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ExpertEditionDto(@JsonProperty("acronym") String acronym) {
        this.acronym = acronym;
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

    public boolean isExpertEdition() {
        return true;
    }

    public boolean isVirtualEdition() {
        return false;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
