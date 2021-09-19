package pt.ist.socialsoftware.edition.notification.dtos.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class EditionSearchOptionDto extends SearchOptionDto {

    private final boolean inclusion;
    private final String edition;
    private final HeteronymSearchOptionDto heteronymSearchOption;
    private final DateSearchOptionDto dateSearchOption;

    @JsonCreator
    public EditionSearchOptionDto(@JsonProperty("inclusion") String inclusion, @JsonProperty("edition") String edition,
                                  @JsonProperty("heteronym") HeteronymSearchOptionDto heteronym, @JsonProperty("date") DateSearchOptionDto date) {

        if (inclusion.equals("in")) {
            this.inclusion = true;
        } else {
            this.inclusion = false;
        }

        this.edition = edition;
        this.heteronymSearchOption = heteronym;
        this.dateSearchOption = date;
    }

    public boolean isInclusion() {
        return this.inclusion;
    }

    public String getEdition() {
        return this.edition;
    }

    public HeteronymSearchOptionDto getHeteronymSearchOption() {
        return this.heteronymSearchOption;
    }

    public DateSearchOptionDto getDateSearchOption() {
        return this.dateSearchOption;
    }

}
