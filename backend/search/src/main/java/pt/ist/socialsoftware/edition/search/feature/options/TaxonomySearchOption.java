package pt.ist.socialsoftware.edition.search.feature.options;

import pt.ist.socialsoftware.edition.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.search.api.dto.SearchableElementDto;
import pt.ist.socialsoftware.edition.search.api.dto.TaxonomySearchOptionDto;

import java.util.Arrays;
import java.util.stream.Stream;

public class TaxonomySearchOption extends SearchOption {
    private final String[] tags;

    public TaxonomySearchOption(TaxonomySearchOptionDto taxonomySearchOptionDto) {
        this.tags = taxonomySearchOptionDto.getTags();
    }

    @Override
    public Stream<SearchableElementDto> search(Stream<SearchableElementDto> inters) {
        return inters.filter(searchableElement -> searchableElement.getType() == SearchableElementDto.Type.VIRTUAL_INTER)
                .filter(i -> verifiesSearchOption(i));
    }

    public boolean verifiesSearchOption(SearchableElementDto inter) {
        SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();

        return Arrays.stream(this.tags)
                .allMatch(tt -> searchRequiresInterface.getVirtualEditionSortedCategoryList(inter.getXmlId())
                        .stream().anyMatch(t -> t.equals(tt)));
    }

}
