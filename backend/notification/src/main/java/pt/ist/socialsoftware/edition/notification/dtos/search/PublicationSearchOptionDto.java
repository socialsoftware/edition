package pt.ist.socialsoftware.edition.notification.dtos.search;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class PublicationSearchOptionDto extends SearchOptionDto {

    private final DateSearchOptionDto dateSearchOption;

    public PublicationSearchOptionDto(@JsonProperty("date") DateSearchOptionDto date) {
        this.dateSearchOption = date;
    }


    public DateSearchOptionDto getDateSearchOption() {
        return this.dateSearchOption;
    }
}