package pt.ist.socialsoftware.edition.search.feature.options;

import pt.ist.socialsoftware.edition.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.search.api.dto.SearchDto;
import pt.ist.socialsoftware.edition.search.api.dto.SearchableElementDto;
import pt.ist.socialsoftware.edition.search.feature.options.SearchOption.Mode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Search {
    public static final String OPTIONS = "options";
    public static final String MODE = "mode";

    private final Mode mode;
    private final SearchOption[] searchOptions;

    public Search(SearchDto searchDto) {
        this.mode = searchDto.getMode();
        this.searchOptions = Arrays.stream(searchDto.getSearchOptions()).map(searchOptionDto -> searchOptionDto.createSearchOption()).toArray(SearchOption[]::new);
    }


    public Mode getMode() {
        return this.mode;
    }

    public SearchOption[] getSearchOptions() {
        return this.searchOptions;
    }

    public Map<String, Map<SearchableElementDto, List<SearchOption>>> search() {
        Set<SearchOption> options = Arrays.stream(getSearchOptions()).collect(Collectors.toSet());
        Map<String, Map<SearchableElementDto, List<SearchOption>>> resultSet = null;

        if (getMode().equals(Mode.OR)) {
            resultSet = searchOptionsORComposition(options);
        }

        if (getMode().equals(Mode.AND)) {
            resultSet = searchOptionsANDComposition(options);
        }

        return resultSet;
    }

    private Map<String, Map<SearchableElementDto, List<SearchOption>>> searchOptionsORComposition(Set<SearchOption> options) {
        Map<String, Map<SearchableElementDto, List<SearchOption>>> resultSet = new LinkedHashMap<>();

        List<SearchOption> searchOptions = orderTextSearchOptions(options);

        List<SearchableElementDto> searchableElementDtos = getSearchableElements();

        for (SearchOption searchOption : searchOptions) {
            for (SearchableElementDto inter : searchOption.search(searchableElementDtos.stream()).collect(Collectors.toList())) {
                addToMatchSet(resultSet, inter, searchOption);
            }
        }

        return resultSet;
    }

    private Map<String, Map<SearchableElementDto, List<SearchOption>>> searchOptionsANDComposition(Set<SearchOption> options) {
        Map<String, Map<SearchableElementDto, List<SearchOption>>> resultSet = new LinkedHashMap<>();

        List<SearchOption> searchOptions = orderTextSearchOptions(options);

        List<SearchableElementDto> searchableElementDtos = getSearchableElements();

        Stream<SearchableElementDto> selectedElements = searchableElementDtos.stream();
        for (SearchOption searchOption : searchOptions) {
            List<SearchableElementDto> inters = searchOption.search(selectedElements).collect(Collectors.toList());

            for (SearchableElementDto inter : inters) {
                addToMatchSet(resultSet, inter, searchOption);
            }

            Set<String> fragmentXmlIds = inters.stream().map(searchableElement -> searchableElement.getFragmentXmlId()).collect(Collectors.toSet());

            selectedElements = searchableElementDtos.stream().filter(searchableElement -> fragmentXmlIds.contains(searchableElement.getFragmentXmlId()));
        }

        purgeNonFullyAchievedEntries(resultSet, searchOptions);

        return resultSet;
    }

    private List<SearchableElementDto> getSearchableElements() {
        SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();

        return Stream.concat(searchRequiresInterface.getFragmentDtoSet().stream()
                        .flatMap(f -> f.getScholarInterDtoSet().stream()).map(scholarInter ->
                                new SearchableElementDto(SearchableElementDto.Type.SCHOLAR_INTER, scholarInter.getXmlId(),
                                        scholarInter.getTitle(), scholarInter.getFragmentXmlId(), scholarInter.getUrlId(),
                                        scholarInter.getShortName(), scholarInter.getXmlId()))
                , searchRequiresInterface.getVirtualEditionInterSet().stream().map(virtualEditionInter ->
                        new SearchableElementDto(SearchableElementDto.Type.VIRTUAL_INTER, virtualEditionInter.getXmlId(),
                                virtualEditionInter.getTitle(), virtualEditionInter.getFragmentXmlId(), virtualEditionInter.getUrlId(),
                                virtualEditionInter.getShortName(), virtualEditionInter.getLastUsed().getXmlId()))
        ).collect(Collectors.toList());
    }

    private void purgeNonFullyAchievedEntries(Map<String, Map<SearchableElementDto, List<SearchOption>>> resultSet,
                                              List<SearchOption> searchOptions) {
        for (Map.Entry<String, Map<SearchableElementDto, List<SearchOption>>> resultEntry : resultSet.entrySet()) {
            Set<SearchOption> achievedOptions = new HashSet<>();
            for (Map.Entry<SearchableElementDto, List<SearchOption>> entry : resultEntry.getValue().entrySet()) {
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

    private void addToMatchSet(Map<String, Map<SearchableElementDto, List<SearchOption>>> matchSet, SearchableElementDto inter,
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
