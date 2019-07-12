package pt.ist.socialsoftware.edition.ldod.search.feature.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.search.api.dto.SearchableElementDto;

import java.util.stream.Stream;

public final class PublicationSearchOption extends SearchOption {

    private final DateSearchOption dateSearchOption;

    public PublicationSearchOption(@JsonProperty("date") DateSearchOption date) {
        this.dateSearchOption = date;
    }

    @Override
    public String toString() {
        return "publication:" + this.dateSearchOption;
    }

    @Override
    public Stream<SearchableElementDto> search(Stream<SearchableElementDto> inters) {
        return inters.filter(i -> verifiesSearchOption(i));
    }

    private boolean verifiesSearchOption(SearchableElementDto inter) {
        SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();

        return inter.getType().equals(SearchableElementDto.Type.SCHOLAR_INTER) && searchRequiresInterface.isSourceInter(inter.getXmlId()) && searchRequiresInterface.getSourceInterType(inter.getXmlId()).equals(Source.SourceType.PRINTED.name())
                && this.dateSearchOption.verifiesSearchOption(inter);
    }

    public boolean hasDate() {
        return this.dateSearchOption == null ? false : this.dateSearchOption.hasDate();
    }

}