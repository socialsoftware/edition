package pt.ist.socialsoftware.edition.ldod.search.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.search.SearchableElement;

import java.util.stream.Stream;

public final class HeteronymSearchOption extends SearchOption {
    private static final Logger logger = LoggerFactory.getLogger(HeteronymSearchOption.class);

    private final String xmlId4Heteronym;

    public HeteronymSearchOption(@JsonProperty("heteronym") String xmlId) {
        logger.debug("HeteronymSearchOption xmlId: {}", xmlId);
        this.xmlId4Heteronym = xmlId.equals("null") ? null : xmlId;
    }

    @Override
    public String toString() {
        return "heteronym:" + this.xmlId4Heteronym;
    }

    @Override
    public Stream<SearchableElement> search(Stream<SearchableElement> inters) {
        return inters.filter(i -> verifiesSearchOption(i));
    }

    public boolean verifiesSearchOption(SearchableElement inter) {
        TextInterface textInterface = new TextInterface();

        if (ALL.equals(this.xmlId4Heteronym)) {
            // all are selected
            return true;
        } else if (this.xmlId4Heteronym == null && textInterface.getHeteronymXmlId(inter.getXmlId()) == null) {
            // Searching for fragments with no authors and fragment has no
            // author
            return true;
        } else if ((this.xmlId4Heteronym != null && textInterface.getHeteronymXmlId(inter.getXmlId()) == null)
                || (this.xmlId4Heteronym == null && textInterface.getHeteronymXmlId(inter.getXmlId()) != null)) {
            // Searching for fragment with author and fragment has no author or
            // searching for fragment with no author and fragment has author and
            return false;
        } else {
            // the interpretation has the expected correct heteronym assignment
            return this.xmlId4Heteronym.equals(textInterface.getHeteronymXmlId(inter.getXmlId()));
        }
    }

    public boolean hasHeteronym() {
        return this.xmlId4Heteronym != null && !this.xmlId4Heteronym.equals(ALL);
    }
}
