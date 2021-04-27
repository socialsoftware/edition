package pt.ist.socialsoftware.edition.search.api;


import pt.ist.socialsoftware.edition.search.api.dto.AdvancedSearchResultDto;
import pt.ist.socialsoftware.edition.search.api.dto.SearchDto;
import pt.ist.socialsoftware.edition.search.api.dto.VirtualEditionSearchOptionDto;
import pt.ist.socialsoftware.edition.search.feature.SearchProcessor;
import pt.ist.socialsoftware.edition.virtual.api.textDto.ScholarInterDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SearchProvidesInterface {
    private final SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();

    public Map<String, List<ScholarInterDto>> simpleSearch(String params) {
        return new SearchProcessor().simpleSearch(params);
    }

    public AdvancedSearchResultDto advancedSearch(SearchDto search, String username) {

        if (username != null) {
            Arrays.stream(search.getSearchOptions())
                .filter(searchOptionDto -> searchOptionDto instanceof VirtualEditionSearchOptionDto)
                .forEach(searchOptionDto -> ((VirtualEditionSearchOptionDto) searchOptionDto).setUsername(username));
        }

        return new SearchProcessor().advancedSearch(search);
    }

}
