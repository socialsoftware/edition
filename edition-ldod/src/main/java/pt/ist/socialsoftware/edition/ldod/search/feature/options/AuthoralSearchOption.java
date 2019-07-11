package pt.ist.socialsoftware.edition.ldod.search.feature.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.ldod.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.search.feature.SearchableElement;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.SourceDto;

import java.util.stream.Stream;

public abstract class AuthoralSearchOption extends SearchOption {

    private final String hasLdoD;
    private final DateSearchOption dateSearchOption;

    public AuthoralSearchOption(@JsonProperty("hasLdoDMark") String hasLdoD,
                                @JsonProperty("date") DateSearchOption date) {
        this.hasLdoD = hasLdoD;
        this.dateSearchOption = date;
    }

    @Override
    public Stream<SearchableElement> search(Stream<SearchableElement> inters) {
        return inters.filter(searchableElement -> searchableElement.getType() == SearchableElement.Type.SCHOLAR_INTER)
                .filter(i -> verifiesSearchOption(i));
    }

    private boolean verifiesSearchOption(SearchableElement inter) {
        SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();

        if (searchRequiresInterface.isSourceInter(inter.getXmlId()) && searchRequiresInterface.getSourceType(inter.getXmlId(), SourceType.MANUSCRIPT)) {
            SourceDto source = searchRequiresInterface.getSourceOfInter(inter.getXmlId());
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

    protected abstract String getDocumentType();

    @Override
    public String toString() {
        return getDocumentType() + ": hasLdoDMark:" + this.hasLdoD + "\n" + this.dateSearchOption;
    }

    public boolean hasDate() {
        return this.dateSearchOption == null ? false : this.dateSearchOption.hasDate();
    }

    public boolean hasLdoDMark() {
        return !this.hasLdoD.equals(ALL);
    }
}
