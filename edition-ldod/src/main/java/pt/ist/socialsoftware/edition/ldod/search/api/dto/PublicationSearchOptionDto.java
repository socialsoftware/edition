package pt.ist.socialsoftware.edition.ldod.search.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.DateSearchOption;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.PublicationSearchOption;

public final class PublicationSearchOptionDto extends SearchOptionDto {

    private final DateSearchOption dateSearchOption;

    public PublicationSearchOptionDto(@JsonProperty("date") DateSearchOption date) {
        this.dateSearchOption = date;
    }

    @Override
    public PublicationSearchOption createSearchOption() {
        return new PublicationSearchOption(this);
    }

    public DateSearchOption getDateSearchOption() {
        return this.dateSearchOption;
    }
}