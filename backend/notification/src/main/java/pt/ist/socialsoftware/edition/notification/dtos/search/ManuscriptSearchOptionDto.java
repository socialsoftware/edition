package pt.ist.socialsoftware.edition.notification.dtos.search;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class ManuscriptSearchOptionDto extends AuthoralSearchOptionDto {
    public static final String MANUSCRIPTID = "manus";

    public ManuscriptSearchOptionDto(@JsonProperty("hasLdoDMark") String hasLdoD,
                                     @JsonProperty("date") DateSearchOptionDto date) {
        super(hasLdoD, date);
    }

}
