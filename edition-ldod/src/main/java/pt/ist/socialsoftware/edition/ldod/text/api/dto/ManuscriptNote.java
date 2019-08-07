package pt.ist.socialsoftware.edition.ldod.text.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.HandNote;
import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.ldod.domain.TypeNote;

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

    public ManuscriptSource.Medium getMedium() {
        return this.medium;
    }

    public String getNote() {
        return this.note;
    }
}
