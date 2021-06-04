package pt.ist.socialsoftware.edition.ldod.frontend.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class TypescriptSearchOptionDto extends AuthoralSearchOptionDto {
    public static final String TYPESCRIPT = "datil";

    public TypescriptSearchOptionDto(@JsonProperty("hasLdoDMark") String hasLdoD,
                                     @JsonProperty("date") DateSearchOptionDto date) {
        super(hasLdoD, date);
    }


}