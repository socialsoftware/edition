package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.search.SearchableElement;
import pt.ist.socialsoftware.edition.ldod.search.options.SearchOption.Mode;

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
		return mode;
	}

	public SearchOption[] getSearchOptions() {
		return searchOptions;
	}

	public Map<Fragment, Map<SearchableElement, List<SearchOption>>> search() {
		Set<SearchOption> options = Arrays.stream(getSearchOptions()).collect(Collectors.toSet());
		Map<Fragment, Map<SearchableElement, List<SearchOption>>> resultSet = null;

		if (getMode().equals(Mode.OR)) {
			resultSet = searchOptionsORComposition(options);
		}

		if (getMode().equals(Mode.AND)) {
			resultSet = searchOptionsANDComposition(options);
		}

		return resultSet;
	}

	private Map<Fragment, Map<SearchableElement, List<SearchOption>>> searchOptionsORComposition(Set<SearchOption> options) {
		Map<Fragment, Map<SearchableElement, List<SearchOption>>> resultSet = new LinkedHashMap<Fragment, Map<SearchableElement, List<SearchOption>>>();

		TextInterface textInterface = new TextInterface();
		List<SearchOption> searchOptions = orderTextSearchOptions(options);

		Stream<SearchableElement> searchableElements = Stream.concat(textInterface.getFragmentsSet().stream()
				.flatMap(f -> f.getScholarInterSet().stream()).map(scholarInter -> new SearchableElement(SearchableElement.Type.SCHOLAR_INTER, scholarInter.getXmlId(), scholarInter.getTitle(),scholarInter.getFragment().getXmlId()))
				,LdoD.getInstance().getVirtualEditionInterSet().stream().map(virtualEditionInter -> new SearchableElement(SearchableElement.Type.VIRTUAL_INTER, virtualEditionInter.getXmlId(), virtualEditionInter.getTitle(),virtualEditionInter.getFragment().getXmlId()))
		);
		for (SearchOption searchOption : searchOptions) {
			for (SearchableElement inter : searchOption.search(searchableElements).collect(Collectors.toList())) {
				addToMatchSet(resultSet, inter, searchOption);
			}
		}

		return resultSet;
	}

	private Map<Fragment, Map<SearchableElement, List<SearchOption>>> searchOptionsANDComposition(Set<SearchOption> options) {
		Map<Fragment, Map<SearchableElement, List<SearchOption>>> resultSet = new LinkedHashMap<Fragment, Map<SearchableElement, List<SearchOption>>>();

		TextInterface textInterface = new TextInterface();
		List<SearchOption> searchOptions = orderTextSearchOptions(options);

		List<SearchableElement> searchableElements = Stream.concat(textInterface.getFragmentsSet().stream()
						.flatMap(f -> f.getScholarInterSet().stream()).map(scholarInter -> new SearchableElement(SearchableElement.Type.SCHOLAR_INTER, scholarInter.getXmlId(), scholarInter.getTitle(),scholarInter.getFragment().getXmlId()))
				,LdoD.getInstance().getVirtualEditionInterSet().stream().map(virtualEditionInter -> new SearchableElement(SearchableElement.Type.VIRTUAL_INTER, virtualEditionInter.getXmlId(), virtualEditionInter.getTitle(),virtualEditionInter.getFragment().getXmlId())))
				.collect(Collectors.toList());

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

	private void purgeNonFullyAchievedEntries(Map<Fragment, Map<SearchableElement, List<SearchOption>>> resultSet,
			List<SearchOption> searchOptions) {
		for (Map.Entry<Fragment, Map<SearchableElement, List<SearchOption>>> resultEntry : resultSet.entrySet()) {
			Set<SearchOption> achievedOptions = new HashSet<SearchOption>();
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
		List<SearchOption> result = new ArrayList<SearchOption>(textSearchOptions);
		result.addAll(options);
		// taxonomy search options last
		result.addAll(taxonomySearchOptions);

		return result;
	}

	private void addToMatchSet(Map<Fragment, Map<SearchableElement, List<SearchOption>>> matchSet, SearchableElement inter,
			SearchOption searchOption) {
		// no entry to fragment
		TextInterface textInterface = new TextInterface();
		Fragment fragment = textInterface.getFragmentByInterXmlId(inter.getXmlId());
		if (fragment == null)
			fragment = LdoD.getInstance().getVirtualEditionInterByXmlId(inter.getXmlId()).getFragment();

		if (matchSet.get(fragment) == null) {
			matchSet.put(fragment, new HashMap<SearchableElement, List<SearchOption>>());
		}

		// no entry to fragInter
		if (matchSet.get(fragment).get(inter) == null) {
			matchSet.get(fragment).put(inter, new ArrayList<SearchOption>());
		}

		// insert element
		matchSet.get(fragment).get(inter).add(searchOption);
	}

}
