package pt.ist.socialsoftware.edition.text.api.dto;


import pt.ist.socialsoftware.edition.text.domain.AnnexNote;

public class AnnexNoteDto {

    private int number;

    private String text;

    public AnnexNoteDto(AnnexNote note){
        setNumber(note.getNumber());
        setText(note.getNoteText().generatePresentationText());
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
