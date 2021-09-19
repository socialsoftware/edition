package pt.ist.socialsoftware.edition.notification.dtos.text;



public class AnnexNoteDto {

    private int number;

    private String text;

    public AnnexNoteDto() {
        super();
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
