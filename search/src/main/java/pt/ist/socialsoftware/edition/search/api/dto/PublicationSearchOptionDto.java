package pt.ist.socialsoftware.edition.search.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.search.feature.options.PublicationSearchOption;

public final class PublicationSearchOptionDto extends SearchOptionDto {

    private final DateSearchOptionDto dateSearchOption;

    public PublicationSearchOptionDto(@JsonProperty("date") DateSearchOptionDto date) {
        this.dateSearchOption = date;
    }

    @Override
    public PublicationSearchOption createSearchOption() {
        return new PublicationSearchOption(this);
    }

    public DateSearchOptionDto getDateSearchOption() {
        return this.dateSearchOption;
    }
}