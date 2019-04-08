package pt.ist.socialsoftware.edition.ldod.search.options;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.search.SearchableElement;

import java.util.stream.Stream;

public final class EditionSearchOption extends SearchOption {

    private final boolean inclusion;
    private final String edition;
    private final HeteronymSearchOption heteronymSearchOption;
    private final DateSearchOption dateSearchOption;

    @JsonCreator
    public EditionSearchOption(@JsonProperty("inclusion") String inclusion, @JsonProperty("edition") String edition,
                               @JsonProperty("heteronym") HeteronymSearchOption heteronym, @JsonProperty("date") DateSearchOption date) {

        if (inclusion.equals("in")) {
            this.inclusion = true;
        } else {
            this.inclusion = false;
        }

        this.edition = edition;
        this.heteronymSearchOption = heteronym;
        this.dateSearchOption = date;
    }

    @Override
    public String toString() {
        return "edition:" + this.edition + "\ninclusion:" + this.inclusion + "\n" + this.heteronymSearchOption + "\n" + this.dateSearchOption;
    }

    @Override
    public Stream<SearchableElement> search(Stream<SearchableElement> inters) {
        return inters.filter(searchableElement -> searchableElement.getType() == SearchableElement.Type.SCHOLAR_INTER)
                .filter(i -> verifiesSearchOption(i));
    }

    private boolean verifiesSearchOption(SearchableElement inter) {
        TextInterface textInterface = new TextInterface();
        if (textInterface.isExpertInter(inter.getXmlId()) && this.inclusion) {
            if (this.edition.equals(ALL)) {
                return true;
            }
            if (!(this.edition.equals(textInterface.getEditionAcronymOfInter(inter.getXmlId())))) {
                return false;
            }
            if (this.heteronymSearchOption != null && !this.heteronymSearchOption.verifiesSearchOption(inter)) {
                return false;
            }
            if (this.dateSearchOption != null && !this.dateSearchOption.verifiesSearchOption(inter)) {
                return false;
            }
        } else if ((this.edition.equals(textInterface.getEditionAcronymOfInter(inter.getXmlId())) || this.edition.equals(ALL)) || (this.heteronymSearchOption != null
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
