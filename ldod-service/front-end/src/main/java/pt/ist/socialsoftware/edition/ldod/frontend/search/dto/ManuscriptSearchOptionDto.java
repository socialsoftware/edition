package pt.ist.socialsoftware.edition.ldod.frontend.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class ManuscriptSearchOptionDto extends AuthoralSearchOptionDto {
    public static final String MANUSCRIPTID = "manus";

    public ManuscriptSearchOptionDto(@JsonProperty("hasLdoDMark") String hasLdoD,
                                     @JsonProperty("date") DateSearchOptionDto date) {
        super(hasLdoD, date);
    }

}
