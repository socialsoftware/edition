package pt.ist.socialsoftware.edition.ldod.bff.text.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.ldod.domain.TypeNote;

public class TypeNoteDto {

    private String medium;
    private String note;

    public TypeNoteDto(TypeNote typeNote) {
        setNote(typeNote.getNote());
        setMedium(typeNote.getMedium());

    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(ManuscriptSource.Medium medium) {
        if (medium != null) this.medium = medium.getDesc();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
