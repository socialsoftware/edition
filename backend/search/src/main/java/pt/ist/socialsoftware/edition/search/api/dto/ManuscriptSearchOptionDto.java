package pt.ist.socialsoftware.edition.search.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.search.feature.options.ManuscriptSearchOption;

public final class ManuscriptSearchOptionDto extends AuthoralSearchOptionDto {
    public static final String MANUSCRIPTID = "manus";

    public ManuscriptSearchOptionDto(@JsonProperty("hasLdoDMark") String hasLdoD,
                                     @JsonProperty("date") DateSearchOptionDto date) {
        super(hasLdoD, date);
    }

    @Override
    public ManuscriptSearchOption createSearchOption() {
        return new ManuscriptSearchOption(this);
    }
}
