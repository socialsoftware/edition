package pt.ist.socialsoftware.edition.ldod.search.feature;

import pt.ist.socialsoftware.edition.ldod.search.api.dto.AdvancedSearchResultDto;
import pt.ist.socialsoftware.edition.ldod.search.api.dto.SearchableElementDto;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.*;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchProcessor {

    public Map<String, List<ScholarInterDto>> simpleSearch(String params) {
        String split;
        if (params.contains("&")) {
            split = "&";
        } else {
            split = "%26";
        }

        String search = params.substring(0, params.indexOf(split));
        params = params.substring(params.indexOf(split) + 1);
        String searchType = params.substring(0, params.indexOf(split));
        params = params.substring(params.indexOf(split) + 1);
        String searchSource = params;

        search = TextSearchOption.purgeSearchText(search);

        TextSearchOption textSearchOption = new TextSearchOption(search);
        List<ScholarInterDto> matches = textSearchOption.search();

        Map<String, List<ScholarInterDto>> results = new HashMap<>();
        for (ScholarInterDto inter : matches) {
            String fragmentXmlId = inter.getFragmentXmlId();
            if ((searchSource.compareTo("") == 0
                    || inter.getShortName().toLowerCase().contains(searchSource.toLowerCase()))
                    && (searchType.compareTo("") == 0
                    || inter.getTitle().toLowerCase().contains(search.toLowerCase()))) {

                if (!results.containsKey(fragmentXmlId)) {
                    results.put(fragmentXmlId, new ArrayList<>());
                }

                if (!results.get(fragmentXmlId).contains(inter)) {
                    results.get(fragmentXmlId).add(inter);
                }
            }
        }
        return results;
    }

    public AdvancedSearchResultDto advancedSearch(Search search) {
        Map<String, Map<SearchableElementDto, List<SearchOption>>> results = search.search();

        int fragCount = 0;
        int fragCountNotAdded = 0;
        int interCount = 0;
        int interCountNotAdded = 0;
        boolean showSource = false;
        boolean showSourceType = false;
        boolean showEdition = false;
        boolean showHeteronym = false;
        boolean showDate = false;
        boolean showLdoD = false;
        boolean fragAdd = false;
        boolean showTaxonomy = false;

        for (Map.Entry<String, Map<SearchableElementDto, List<SearchOption>>> entry : results.entrySet()) {
            fragAdd = false;
            for (Map.Entry<SearchableElementDto, List<SearchOption>> entry2 : entry.getValue().entrySet()) {
                if (entry2.getValue().size() >= 1) {
                    interCount++;
                    fragAdd = true;
                } else {
                    interCountNotAdded++;
                }
                for (SearchOption option : entry2.getValue()) {
                    if (option instanceof EditionSearchOption) {
                        showSource = true;
                        EditionSearchOption op = (EditionSearchOption) option;
                        if (!op.getEdition().equals(SearchOption.ALL)) {
                            showEdition = true;
                        }
                        if (op.hasHeteronym()) {
                            showHeteronym = true;
                        }
                        if (op.hasDate()) {
                            showDate = true;
                        }
                    } else if (option instanceof AuthoralSearchOption) {
                        showSource = true;
                        showSourceType = true;
                        AuthoralSearchOption op = (AuthoralSearchOption) option;
                        if (op.hasLdoDMark()) {
                            showLdoD = true;
                        }
                        if (op.hasDate()) {
                            showDate = true;
                        }
                    } else if (option instanceof PublicationSearchOption) {
                        showSource = true;
                        PublicationSearchOption op = (PublicationSearchOption) option;
                        if (op.hasDate()) {
                            showDate = true;
                        }
                    } else if (option instanceof HeteronymSearchOption) {
                        showHeteronym = true;
                    } else if (option instanceof DateSearchOption) {
                        showDate = true;
                    } else if (option instanceof TaxonomySearchOption) {
                        showTaxonomy = true;
                    } else if (option instanceof VirtualEditionSearchOption) {
                        showSource = true;
                    }
                }
            }
            if (fragAdd) {
                fragCount++;
            } else {
                fragCountNotAdded++;
            }
        }
        return new AdvancedSearchResultDto(showEdition, showHeteronym, showDate, showLdoD, showSource, showSourceType,
                showTaxonomy, fragCount, interCount, fragCountNotAdded, interCountNotAdded, results);
    }

}
