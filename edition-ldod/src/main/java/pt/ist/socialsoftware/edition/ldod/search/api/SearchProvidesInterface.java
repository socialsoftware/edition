package pt.ist.socialsoftware.edition.ldod.search.api;

import pt.ist.socialsoftware.edition.ldod.search.api.dto.AdvancedSearchResultDto;
import pt.ist.socialsoftware.edition.ldod.search.api.dto.SearchDto;
import pt.ist.socialsoftware.edition.ldod.search.feature.SearchProcessor;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;

import java.util.List;
import java.util.Map;

public class SearchProvidesInterface {

    public Map<String, List<ScholarInterDto>> simpleSearch(String params) {
        return new SearchProcessor().simpleSearch(params);
    }

    public AdvancedSearchResultDto advancedSearch(SearchDto search) {
        return new SearchProcessor().advancedSearch(search);
    }
}
