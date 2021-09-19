package pt.ist.socialsoftware.edition.notification.dtos.search;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class TypescriptSearchOptionDto extends AuthoralSearchOptionDto {
    public static final String TYPESCRIPT = "datil";

    public TypescriptSearchOptionDto(@JsonProperty("hasLdoDMark") String hasLdoD,
                                     @JsonProperty("date") DateSearchOptionDto date) {
        super(hasLdoD, date);
    }


}