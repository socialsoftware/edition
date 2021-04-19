package pt.ist.socialsoftware.edition.text.api.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.text.domain.HandNote;
import pt.ist.socialsoftware.edition.text.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.text.domain.TypeNote;

public class ManuscriptNote {
    private final ManuscriptSource.Medium medium;
    private final String note;

    public ManuscriptNote(HandNote handNote) {
        this.medium = handNote.getMedium();
        this.note = handNote.getNote();

    }

    public ManuscriptNote(TypeNote typeNote) {
        this.medium = typeNote.getMedium();
        this.note = typeNote.getNote();
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ManuscriptNote(@JsonProperty("medium") ManuscriptSource.Medium medium, @JsonProperty("note") String note) {
        this.medium = medium;
        this.note = note;
    }

    public ManuscriptSource.Medium getMedium() {
        return this.medium;
    }

    public String getNote() {
        return this.note;
    }
}
