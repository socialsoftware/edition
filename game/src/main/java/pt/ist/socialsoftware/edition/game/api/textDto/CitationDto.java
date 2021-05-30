package pt.ist.socialsoftware.edition.game.api.textDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CitationDto {



    private long id;
    private String externalId;
    private String date;
    private String fragmentXmlId;
    private String fragmentTitle;
    private String sourceLink;
    private boolean hasNoInfoRange;

    public CitationDto() {
        super();
    }

    public long getId() {
        return id;
    }

    public boolean isTwitterCitation() {
        return false;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getDate() {
        return date;
    }

    public String getFragmentXmlId() {
        return fragmentXmlId;
    }

    public String getFragmentTitle() {
        return fragmentTitle;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public boolean isHasNoInfoRange() {
        return hasNoInfoRange;
    }

    public LocalDateTime getFormatedDate() {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        return LocalDateTime.parse(getDate(), formater);
    }

}
