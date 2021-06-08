package pt.ist.socialsoftware.edition.search.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.search.feature.options.EditionSearchOption;

public final class EditionSearchOptionDto extends SearchOptionDto {

    private final boolean inclusion;
    private final String edition;
    private final HeteronymSearchOptionDto heteronymSearchOption;
    private final DateSearchOptionDto dateSearchOption;

    @JsonCreator
    public EditionSearchOptionDto(@JsonProperty("inclusion") boolean inclusion, @JsonProperty("edition") String edition,
                                  @JsonProperty("heteronym") HeteronymSearchOptionDto heteronym, @JsonProperty("date") DateSearchOptionDto date) {

        this.inclusion = inclusion;
        this.edition = edition;
        this.heteronymSearchOption = heteronym;
        this.dateSearchOption = date;
    }

    @Override
    public EditionSearchOption createSearchOption() {
        return new EditionSearchOption(this);
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
