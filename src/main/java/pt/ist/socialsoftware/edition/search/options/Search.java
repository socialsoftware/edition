package pt.ist.socialsoftware.edition.search.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.search.options.SearchOption.Mode;

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
		int numberOfSearchOptions = options.size();
		Map<Fragment, Map<FragInter, List<SearchOption>>> resultSet = new LinkedHashMap<Fragment, Map<FragInter, List<SearchOption>>>();

		// the search by text is done in bulk for optimization
		TextSearchOption textSearchOption = null;
		for (SearchOption searchOption : options) {
			if (searchOption instanceof TextSearchOption) {
				textSearchOption = (TextSearchOption) searchOption;
				for (FragInter inter : textSearchOption.search()) {
					addToMatchSet(resultSet, inter, textSearchOption);
				}
			}
		}

		options = options.stream().filter(o -> !(o instanceof TextSearchOption)).collect(Collectors.toSet());

		// the search is done for the other criteria
		for (FragInter inter : LdoD.getInstance().getFragmentsSet().stream()
				.flatMap(f -> f.getFragmentInterSet().stream()).collect(Collectors.toList())) {
			for (SearchOption option : options) {
				if (inter.accept(option))
					addToMatchSet(resultSet, inter, option);
			}
		}

		// remove all the entries that do not fill all conditions when AND
		if (getMode().equals(Mode.AND)) {
			for (Map.Entry<Fragment, Map<FragInter, List<SearchOption>>> resultEntry : resultSet.entrySet()) {
				for (Map.Entry<FragInter, List<SearchOption>> entry : resultEntry.getValue().entrySet()) {
					if (entry.getValue().size() != numberOfSearchOptions) {
						entry.getValue().clear();
					}
				}
			}
		}

		return resultSet;
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
