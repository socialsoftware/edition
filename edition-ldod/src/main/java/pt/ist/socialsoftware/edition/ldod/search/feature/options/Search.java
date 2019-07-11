package pt.ist.socialsoftware.edition.ldod.search.feature.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.search.feature.SearchableElement;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.SearchOption.Mode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Search {
    private static final String OPTIONS = "options";
    private static final String MODE = "mode";

    private final Mode mode;
    private final SearchOption[] searchOptions;

    public Search(@JsonProperty(Search.MODE) String mode, @JsonProperty(Search.OPTIONS) SearchOption[] searchOptions) {
        this.mode = mode.equals(Mode.AND.getMode()) ? Mode.AND : Mode.OR;
        this.searchOptions = searchOptions;
    }

    public Mode getMode() {
        return this.mode;
    }

    public SearchOption[] getSearchOptions() {
        return this.searchOptions;
    }

    public Map<String, Map<SearchableElement, List<SearchOption>>> search() {
        Set<SearchOption> options = Arrays.stream(getSearchOptions()).collect(Collectors.toSet());
        Map<String, Map<SearchableElement, List<SearchOption>>> resultSet = null;

        if (getMode().equals(Mode.OR)) {
            resultSet = searchOptionsORComposition(options);
        }

        if (getMode().equals(Mode.AND)) {
            resultSet = searchOptionsANDComposition(options);
        }

        return resultSet;
    }

    private Map<String, Map<SearchableElement, List<SearchOption>>> searchOptionsORComposition(Set<SearchOption> options) {
        Map<String, Map<SearchableElement, List<SearchOption>>> resultSet = new LinkedHashMap<>();

        List<SearchOption> searchOptions = orderTextSearchOptions(options);

        List<SearchableElement> searchableElements = getSearchableElements();

        for (SearchOption searchOption : searchOptions) {
            for (SearchableElement inter : searchOption.search(searchableElements.stream()).collect(Collectors.toList())) {
                addToMatchSet(resultSet, inter, searchOption);
            }
        }

        return resultSet;
    }

    private Map<String, Map<SearchableElement, List<SearchOption>>> searchOptionsANDComposition(Set<SearchOption> options) {
        Map<String, Map<SearchableElement, List<SearchOption>>> resultSet = new LinkedHashMap<>();

        List<SearchOption> searchOptions = orderTextSearchOptions(options);

        List<SearchableElement> searchableElements = getSearchableElements();

        Stream<SearchableElement> selectedElements = searchableElements.stream();
        for (SearchOption searchOption : searchOptions) {
            List<SearchableElement> inters = searchOption.search(selectedElements).collect(Collectors.toList());

            for (SearchableElement inter : inters) {
                addToMatchSet(resultSet, inter, searchOption);
            }

            Set<String> fragmentXmlIds = inters.stream().map(searchableElement -> searchableElement.getFragmentXmlId()).collect(Collectors.toSet());

            selectedElements = searchableElements.stream().filter(searchableElement -> fragmentXmlIds.contains(searchableElement.getFragmentXmlId()));
        }

        purgeNonFullyAchievedEntries(resultSet, searchOptions);

        return resultSet;
    }

    private List<SearchableElement> getSearchableElements() {
        SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();

        return Stream.concat(searchRequiresInterface.getFragmentDtoSet().stream()
                        .flatMap(f -> f.getScholarInterDtoSet().stream()).map(scholarInter ->
                                new SearchableElement(SearchableElement.Type.SCHOLAR_INTER, scholarInter.getXmlId(), scholarInter.getTitle(), scholarInter.getFragmentXmlId(), scholarInter.getUrlId(), scholarInter.getShortName(), scholarInter.getXmlId()))
                , searchRequiresInterface.getVirtualEditionInterSet().stream().map(virtualEditionInter ->
                        new SearchableElement(SearchableElement.Type.VIRTUAL_INTER, virtualEditionInter.getXmlId(), virtualEditionInter.getTitle(), virtualEditionInter.getFragmentXmlId(), virtualEditionInter.getUrlId(), virtualEditionInter.getShortName(), virtualEditionInter.getUsesScholarInterId()))
        ).collect(Collectors.toList());
    }

    private void purgeNonFullyAchievedEntries(Map<String, Map<SearchableElement, List<SearchOption>>> resultSet,
                                              List<SearchOption> searchOptions) {
        for (Map.Entry<String, Map<SearchableElement, List<SearchOption>>> resultEntry : resultSet.entrySet()) {
            Set<SearchOption> achievedOptions = new HashSet<>();
            for (Map.Entry<SearchableElement, List<SearchOption>> entry : resultEntry.getValue().entrySet()) {
                achievedOptions.addAll(entry.getValue());
            }
            if (achievedOptions.size() != searchOptions.size()) {
                resultEntry.getValue().clear();
            }
        }
    }

    private List<SearchOption> orderTextSearchOptions(Set<SearchOption> options) {
        List<SearchOption> textSearchOptions = options.stream().filter(TextSearchOption.class::isInstance)
                .collect(Collectors.toList());

        options.removeAll(textSearchOptions);

        List<SearchOption> taxonomySearchOptions = options.stream().filter(TaxonomySearchOption.class::isInstance)
                .collect(Collectors.toList());

        options.removeAll(taxonomySearchOptions);

        // text search options first
        List<SearchOption> result = new ArrayList<>(textSearchOptions);
        result.addAll(options);
        // taxonomy search options last
        result.addAll(taxonomySearchOptions);

        return result;
    }

    private void addToMatchSet(Map<String, Map<SearchableElement, List<SearchOption>>> matchSet, SearchableElement inter,
                               SearchOption searchOption) {
        // no entry to fragment
        if (matchSet.get(inter.getFragmentXmlId()) == null) {
            matchSet.put(inter.getFragmentXmlId(), new HashMap<>());
        }

        // no entry to fragInter
        if (matchSet.get(inter.getFragmentXmlId()).get(inter) == null) {
            matchSet.get(inter.getFragmentXmlId()).put(inter, new ArrayList<>());
        }

        // insert element
        matchSet.get(inter.getFragmentXmlId()).get(inter).add(searchOption);
    }

}
