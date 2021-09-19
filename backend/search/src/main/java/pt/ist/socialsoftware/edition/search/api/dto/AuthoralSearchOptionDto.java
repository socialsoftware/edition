package pt.ist.socialsoftware.edition.search.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AuthoralSearchOptionDto extends SearchOptionDto {

    private final String hasLdoD;
    private final DateSearchOptionDto dateSearchOption;

    public AuthoralSearchOptionDto(@JsonProperty("hasLdoDMark") String hasLdoD,
                                   @JsonProperty("date") DateSearchOptionDto date) {
        this.hasLdoD = hasLdoD;
        this.dateSearchOption = date;
    }

    public String getHasLdoD() {
        return this.hasLdoD;
    }

    public DateSearchOptionDto getDateSearchOption() {
        return this.dateSearchOption;
    }

}
