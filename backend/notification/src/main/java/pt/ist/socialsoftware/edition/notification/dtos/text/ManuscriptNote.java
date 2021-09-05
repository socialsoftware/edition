package pt.ist.socialsoftware.edition.notification.dtos.text;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.notification.enums.Medium;

public class ManuscriptNote {
    private final Medium medium;
    private final String note;



    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ManuscriptNote(@JsonProperty("medium") Medium medium, @JsonProperty("note") String note) {
        this.medium = medium;
        this.note = note;
    }

    public Medium getMedium() {
        return this.medium;
    }

    public String getNote() {
        return this.note;
    }
}
