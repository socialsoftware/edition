package pt.ist.socialsoftware.edition.ldod.search.feature.options;

import pt.ist.socialsoftware.edition.ldod.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.ldod.frontend.search.dto.AuthoralSearchOptionDto;
import pt.ist.socialsoftware.edition.ldod.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.search.api.dto.SearchableElementDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.SourceDto;

import java.util.stream.Stream;

public abstract class AuthoralSearchOption extends SearchOption {

    private final String hasLdoD;
    private final DateSearchOption dateSearchOption;

    public AuthoralSearchOption(String hasLdoD,
                                DateSearchOption date) {
        this.hasLdoD = hasLdoD;
        this.dateSearchOption = date;
    }

    public AuthoralSearchOption(AuthoralSearchOptionDto authoralSearchOptionDto) {
        this.hasLdoD = authoralSearchOptionDto.getHasLdoD();
        this.dateSearchOption = authoralSearchOptionDto.getDateSearchOption().createSearchOption();
    }

    @Override
    public Stream<SearchableElementDto> search(Stream<SearchableElementDto> inters) {
        return inters.filter(searchableElement -> searchableElement.getType() == SearchableElementDto.Type.SCHOLAR_INTER)
                .filter(i -> verifiesSearchOption(i));
    }

    private boolean verifiesSearchOption(SearchableElementDto inter) {
        SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();

        if (searchRequiresInterface.isSourceInter(inter.getXmlId()) && searchRequiresInterface.getSourceInterType(inter.getXmlId()).equals(SourceType.MANUSCRIPT.name())) {
            SourceDto source = searchRequiresInterface.getSourceOfSourceInter(inter.getXmlId());
            if (isOfDocumentType(source) && this.dateSearchOption.verifiesSearchOption(inter)) {
                if (this.hasLdoD.equals(ALL) || (this.hasLdoD.equals("true") && source.hasLdoDLabel())) {
                    return true;
                } else if (this.hasLdoD.equals("false") && !source.hasLdoDLabel()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected abstract boolean isOfDocumentType(SourceDto source);

    public boolean hasDate() {
        return this.dateSearchOption == null ? false : this.dateSearchOption.hasDate();
    }

    public boolean hasLdoDMark() {
        return !this.hasLdoD.equals(ALL);
    }
}
