package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class TaxonomySearchOption extends SearchOption {
	private String[] tags;

	public TaxonomySearchOption(@JsonProperty("tags") String tags) {
		this.tags = tags.trim().split("\\s+");
	}

	@Override
	public Set<FragInter> search(Set<FragInter> inters) {
		return inters.stream().filter(VirtualEditionInter.class::isInstance).map(VirtualEditionInter.class::cast)
				.filter(i -> verifiesSearchOption(i)).collect(Collectors.toSet());
	}

	public boolean verifiesSearchOption(VirtualEditionInter inter) {
		return Arrays.stream(tags).allMatch(tt -> inter.getTagSet().stream()
				.filter(t -> t.getCategory().getName().equals(tt)).findAny().isPresent());
	}

	@Override
	public String toString() {
		return "Taxonomy :" + Arrays.stream(tags).collect(Collectors.joining(" "));
	}

}
