package pt.ist.socialsoftware.edition.ldod.search.feature.options;

import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.frontend.search.dto.PublicationSearchOptionDto;
import pt.ist.socialsoftware.edition.ldod.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.search.api.dto.SearchableElementDto;

import java.util.stream.Stream;

public final class PublicationSearchOption extends SearchOption {
    private final DateSearchOption dateSearchOption;

    public PublicationSearchOption(PublicationSearchOptionDto publicationSearchOptionDto) {
        this.dateSearchOption = publicationSearchOptionDto.getDateSearchOption().createSearchOption();
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