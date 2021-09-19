package pt.ist.socialsoftware.edition.notification.dtos.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.notification.enums.Mode;


public class SearchDto {
    private final Mode mode;
    private final SearchOptionDto[] searchOptions;

    public SearchDto(@JsonProperty("mode") String mode, @JsonProperty("options") SearchOptionDto[] searchOptions) {
        this.mode = mode.equals(Mode.AND.getMode()) ? Mode.AND : Mode.OR;
        this.searchOptions = searchOptions;
    }

    public Mode getMode() {
        return this.mode;
    }

    public SearchOptionDto[] getSearchOptions() {
        return this.searchOptions;
    }

}
