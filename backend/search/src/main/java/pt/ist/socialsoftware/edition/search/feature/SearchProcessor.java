package pt.ist.socialsoftware.edition.search.feature;


import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.search.api.dto.AdvancedSearchResultDto;
import pt.ist.socialsoftware.edition.search.api.dto.SearchDto;
import pt.ist.socialsoftware.edition.search.api.dto.SearchableElementDto;

import pt.ist.socialsoftware.edition.search.feature.options.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchProcessor {
    //private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

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

    public AdvancedSearchResultDto advancedSearch(SearchDto searchDto) {
       // logger.debug("AdvancedSearchResultDto");

        Map<String, Map<SearchableElementDto, List<SearchOption>>> results = new Search(searchDto).search();

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

        Map<String, Map<SearchableElementDto, List<String>>> resultsDto = results.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, ev -> ev.getValue().entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, ev2 -> ev2.getValue().stream()
                                .map(SearchOption::getClass)
                                .map(Class::getSimpleName)
                                .map(s -> s + "Dto")
                                .collect(Collectors.toList())))));

        //logger.debug("AdvancedSearchResultDto {}", results.entrySet().size());

        return new AdvancedSearchResultDto(showEdition, showHeteronym, showDate, showLdoD, showSource, showSourceType,
                showTaxonomy, fragCount, interCount, fragCountNotAdded, interCountNotAdded, resultsDto);
    }

}
