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

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
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

	public Map<Fragment, Map<FragInter, List<SearchOption>>> search() {
		Set<SearchOption> options = Arrays.stream(getSearchOptions()).collect(Collectors.toSet());
		Map<Fragment, Map<FragInter, List<SearchOption>>> resultSet = null;

		if (getMode().equals(Mode.OR)) {
			resultSet = searchOptionsORComposition(options);
		}

		if (getMode().equals(Mode.AND)) {
			resultSet = searchOptionsANDComposition(options);
		}

		return resultSet;
	}

	private Map<Fragment, Map<FragInter, List<SearchOption>>> searchOptionsORComposition(Set<SearchOption> options) {
		Map<Fragment, Map<FragInter, List<SearchOption>>> resultSet = new LinkedHashMap<Fragment, Map<FragInter, List<SearchOption>>>();

		Set<FragInter> inters = LdoD.getInstance().getFragmentsSet().stream()
				.flatMap(f -> f.getFragmentInterSet().stream()).collect(Collectors.toSet());
		List<SearchOption> searchOptions = orderTextSearchOptions(options);
		for (SearchOption searchOption : searchOptions) {
			for (FragInter inter : searchOption.search(inters)) {
				addToMatchSet(resultSet, inter, searchOption);
			}
		}

		return resultSet;
	}

	private Map<Fragment, Map<FragInter, List<SearchOption>>> searchOptionsANDComposition(Set<SearchOption> options) {
		Map<Fragment, Map<FragInter, List<SearchOption>>> resultSet = new LinkedHashMap<Fragment, Map<FragInter, List<SearchOption>>>();

		Set<FragInter> inters = LdoD.getInstance().getFragmentsSet().stream()
				.flatMap(f -> f.getFragmentInterSet().stream()).collect(Collectors.toSet());
		List<SearchOption> searchOptions = orderTextSearchOptions(options);
		for (SearchOption searchOption : searchOptions) {
			inters = searchOption.search(inters);

			for (FragInter inter : inters) {
				addToMatchSet(resultSet, inter, searchOption);
			}

			inters = inters.stream().flatMap(i -> i.getFragment().getFragmentInterSet().stream())
					.collect(Collectors.toSet());
		}

		purgeNonFullyAchievedEntries(resultSet, searchOptions);

		return resultSet;
	}

	private void purgeNonFullyAchievedEntries(Map<Fragment, Map<FragInter, List<SearchOption>>> resultSet,
			List<SearchOption> searchOptions) {
		for (Map.Entry<Fragment, Map<FragInter, List<SearchOption>>> resultEntry : resultSet.entrySet()) {
			Set<SearchOption> achievedOptions = new HashSet<SearchOption>();
			for (Map.Entry<FragInter, List<SearchOption>> entry : resultEntry.getValue().entrySet()) {
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

	private void addToMatchSet(Map<Fragment, Map<FragInter, List<SearchOption>>> matchSet, FragInter inter,
			SearchOption searchOption) {
		// no entry to fragment
		if (matchSet.get(inter.getFragment()) == null) {
			matchSet.put(inter.getFragment(), new HashMap<FragInter, List<SearchOption>>());
		}

		// no entry to fragInter
		if (matchSet.get(inter.getFragment()).get(inter) == null) {
			matchSet.get(inter.getFragment()).put(inter, new ArrayList<SearchOption>());
		}

		// insert element
		matchSet.get(inter.getFragment()).get(inter).add(searchOption);
	}

}
