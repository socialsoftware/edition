package pt.ist.socialsoftware.edition.ldod.frontend.serverside.search;

import pt.ist.socialsoftware.edition.ldod.search.api.SearchProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.search.api.dto.AdvancedSearchResultDto;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.Search;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;

import java.util.List;
import java.util.Map;

public class SearchFrontendRequiresInterface {
    // Requires from Text Module
    TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public String getFragmentTitle(String fragmentXmlId) {
        return this.textProvidesInterface.getFragmentTitle(fragmentXmlId);
    }

    public List<ScholarInterDto> getExpertEditionScholarInterDtoList(String acronym) {
        return this.textProvidesInterface.getExpertEditionScholarInterDtoList(acronym);
    }

    // Requires from Search Module
    SearchProvidesInterface searchProvidesInterface = new SearchProvidesInterface();

    public Map<String, List<ScholarInterDto>> getSimpleSearch(String params) {
        return this.searchProvidesInterface.simpleSearch(params);
    }

    public AdvancedSearchResultDto advancedSearch(Search search) {
        return this.searchProvidesInterface.advancedSearch(search);
    }
}
