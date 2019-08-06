package pt.ist.socialsoftware.edition.ldod.frontend.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.Search;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.SearchOption.Mode;


public class SearchDto {
    private final Mode mode;
    private final SearchOptionDto[] searchOptions;

    public SearchDto(@JsonProperty(Search.MODE) String mode, @JsonProperty(Search.OPTIONS) SearchOptionDto[] searchOptions) {
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
