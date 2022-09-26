package pt.ist.socialsoftware.edition.ldod.bff.text.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.HandNote;
import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource;

public class HandNoteDto {

    private String medium;
    private String note;

    public HandNoteDto(HandNote handNote) {
        setNote(handNote.getNote());
        setMedium(handNote.getMedium());

    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(ManuscriptSource.Medium medium) {
        if(medium != null) this.medium = medium.getDesc();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
