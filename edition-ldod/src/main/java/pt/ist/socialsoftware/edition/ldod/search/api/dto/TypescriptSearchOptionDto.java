package pt.ist.socialsoftware.edition.ldod.search.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.TypescriptSearchOption;

public final class TypescriptSearchOptionDto extends AuthoralSearchOptionDto {
    public static final String TYPESCRIPT = "datil";

    public TypescriptSearchOptionDto(@JsonProperty("hasLdoDMark") String hasLdoD,
                                     @JsonProperty("date") DateSearchOptionDto date) {
        super(hasLdoD, date);
    }


    @Override
    public TypescriptSearchOption createSearchOption() {
        return new TypescriptSearchOption(this);
    }
}