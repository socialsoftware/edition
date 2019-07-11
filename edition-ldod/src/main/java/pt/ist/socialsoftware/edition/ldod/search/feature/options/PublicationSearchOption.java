package pt.ist.socialsoftware.edition.ldod.search.feature.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.search.feature.SearchableElement;

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
    public Stream<SearchableElement> search(Stream<SearchableElement> inters) {
        return inters.filter(i -> verifiesSearchOption(i));
    }

    private boolean verifiesSearchOption(SearchableElement inter) {
        SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();

        return searchRequiresInterface.isSourceInter(inter.getXmlId()) && searchRequiresInterface.getSourceType(inter.getXmlId(), Source.SourceType.PRINTED)
                && this.dateSearchOption.verifiesSearchOption(inter);
    }

    public boolean hasDate() {
        return this.dateSearchOption == null ? false : this.dateSearchOption.hasDate();
    }

}