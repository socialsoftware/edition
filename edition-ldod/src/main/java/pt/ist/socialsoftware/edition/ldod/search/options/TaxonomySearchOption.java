package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.api.ldod.LdoDInterface;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.search.SearchableElement;

public class TaxonomySearchOption extends SearchOption {
	private String[] tags;

	public TaxonomySearchOption(@JsonProperty("tags") String tags) {
		this.tags = tags.trim().split("\\s+");
	}

	@Override
	public Stream<SearchableElement> search(Stream<SearchableElement> inters) {
		return inters.filter(searchableElement -> searchableElement.getType() == SearchableElement.Type.VIRTUAL_INTER)
				.filter(i -> verifiesSearchOption(i));
	}

	public boolean verifiesSearchOption(SearchableElement inter) {
		LdoDInterface ldoDInterface = new LdoDInterface();

		return Arrays.stream(tags).allMatch(tt -> ldoDInterface.getTagsForVei(inter.getXmlId()).stream()
				.filter(t -> t.getCategory().getName().equals(tt)).findAny().isPresent());
	}

	@Override
	public String toString() {
		return "Taxonomy :" + Arrays.stream(tags).collect(Collectors.joining(" "));
	}

}
