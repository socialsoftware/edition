package pt.ist.socialsoftware.edition.search.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.search.feature.options.Search;
import pt.ist.socialsoftware.edition.search.feature.options.SearchOption;


public class SearchDto {
    private final SearchOption.Mode mode;
    private final SearchOptionDto[] searchOptions;

    public SearchDto(@JsonProperty(Search.MODE) String mode, @JsonProperty(Search.OPTIONS) SearchOptionDto[] searchOptions) {
        this.mode = mode.toLowerCase().equals(SearchOption.Mode.AND.getMode()) ? SearchOption.Mode.AND : SearchOption.Mode.OR;
        this.searchOptions = searchOptions;
    }

    public SearchOption.Mode getMode() {
        return this.mode;
    }

    public SearchOptionDto[] getSearchOptions() {
        return this.searchOptions;
    }

}
