package pt.ist.socialsoftware.edition.search.options;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

public class TaxonomySearchOption extends SearchOption {
	private Set<String> tags;

	public TaxonomySearchOption(@JsonProperty("tags") String tags) {
		this.tags = Arrays.stream(tags.trim().split("\\s+")).collect(Collectors.toSet());
	}

	@Override
	public boolean visit(VirtualEditionInter inter) {
		return inter.getTagSet().stream().map(t -> t.getCategory().getName()).collect(Collectors.toSet())
				.containsAll(tags);
	}

	@Override
	public String toString() {
		return "Taxonomy :" + tags.stream().collect(Collectors.joining(" "));
	}

}
