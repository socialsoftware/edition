package pt.ist.socialsoftware.edition.ldod.search.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.ldod.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.ldod.search.SearchableElement;

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
        TextInterface textInterface = new TextInterface();

        if (textInterface.isSourceInter(inter.getXmlId()) && textInterface.usesSourceType(inter.getXmlId(), SourceType.MANUSCRIPT)) {
            ManuscriptSource source = (ManuscriptSource) textInterface.getSourceOfInter(inter.getXmlId());
            if (isOfDocumentType(source) && this.dateSearchOption.verifiesSearchOption(inter)) {
                if (this.hasLdoD.equals(ALL) || (this.hasLdoD.equals("true") && source.getHasLdoDLabel())) {
                    return true;
                } else if (this.hasLdoD.equals("false") && !source.getHasLdoDLabel()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected abstract boolean isOfDocumentType(ManuscriptSource source);

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
