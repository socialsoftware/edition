package pt.ist.socialsoftware.edition.ldod.search.feature.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.search.api.dto.SearchableElementDto;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaxonomySearchOption extends SearchOption {
    private final String[] tags;

    public TaxonomySearchOption(@JsonProperty("tags") String tags) {
        this.tags = tags.trim().split("\\s+");
    }

    @Override
    public Stream<SearchableElementDto> search(Stream<SearchableElementDto> inters) {
        return inters.filter(searchableElement -> searchableElement.getType() == SearchableElementDto.Type.VIRTUAL_INTER)
                .filter(i -> verifiesSearchOption(i));
    }

    public boolean verifiesSearchOption(SearchableElementDto inter) {
        SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();

        return Arrays.stream(this.tags)
                .allMatch(tt -> searchRequiresInterface.getTagsForVirtualEditionInter(inter.getXmlId())
                        .stream().anyMatch(t -> t.equals(tt)));
    }

    @Override
    public String toString() {
        return "Taxonomy :" + Arrays.stream(this.tags).collect(Collectors.joining(" "));
    }

}
