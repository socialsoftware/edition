package pt.ist.socialsoftware.edition.search.feature.options;

import pt.ist.socialsoftware.edition.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.search.api.dto.EditionSearchOptionDto;
import pt.ist.socialsoftware.edition.search.api.dto.SearchableElementDto;

import java.util.stream.Stream;

public final class EditionSearchOption extends SearchOption {

    private final boolean inclusion;
    private final String edition;
    private final HeteronymSearchOption heteronymSearchOption;
    private final DateSearchOption dateSearchOption;

    public EditionSearchOption(EditionSearchOptionDto editionSearchOptionDto) {
        this.inclusion = editionSearchOptionDto.isInclusion();
        this.edition = editionSearchOptionDto.getEdition();
        this.heteronymSearchOption = editionSearchOptionDto.getHeteronymSearchOption() != null ?
                editionSearchOptionDto.getHeteronymSearchOption().createSearchOption() : null;
        this.dateSearchOption = editionSearchOptionDto.getDateSearchOption() != null ? editionSearchOptionDto.getDateSearchOption().createSearchOption() : null;
    }

    @Override
    public Stream<SearchableElementDto> search(Stream<SearchableElementDto> inters) {
        return inters.filter(searchableElement -> searchableElement.getType() == SearchableElementDto.Type.SCHOLAR_INTER)
                .filter(i -> verifiesSearchOption(i));
    }

    private boolean verifiesSearchOption(SearchableElementDto inter) {
        SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();
        if (searchRequiresInterface.isExpertInter(inter.getXmlId()) && this.inclusion) {
            if (this.edition.equals(ALL)) {
                return true;
            }
            if (!(this.edition.equals(searchRequiresInterface.getEditionAcronymOfInter(inter.getXmlId())))) {
                return false;
            }
            if (this.heteronymSearchOption != null && !this.heteronymSearchOption.verifiesSearchOption(inter)) {
                return false;
            }
            if (this.dateSearchOption != null && !this.dateSearchOption.verifiesSearchOption(inter)) {
                return false;
            }
        } else if ((this.edition.equals(searchRequiresInterface.getEditionAcronymOfInter(inter.getXmlId())) || this.edition.equals(ALL)) || (this.heteronymSearchOption != null
                && this.heteronymSearchOption.verifiesSearchOption(inter)) || (this.dateSearchOption != null && this.dateSearchOption.verifiesSearchOption(inter))) {
            return false;
        }

        return true;
    }

    public boolean hasDate() {
        return this.dateSearchOption == null ? false : this.dateSearchOption.hasDate();
    }

    public boolean hasHeteronym() {
        return this.heteronymSearchOption == null ? false : this.heteronymSearchOption.hasHeteronym();
    }

    public String getEdition() {
        return this.edition;
    }
}
