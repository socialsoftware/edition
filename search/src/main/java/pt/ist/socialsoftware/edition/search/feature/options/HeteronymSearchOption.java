package pt.ist.socialsoftware.edition.search.feature.options;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.search.api.dto.HeteronymSearchOptionDto;
import pt.ist.socialsoftware.edition.search.api.dto.SearchableElementDto;

import java.util.stream.Stream;

public final class HeteronymSearchOption extends SearchOption {
    private static final Logger logger = LoggerFactory.getLogger(HeteronymSearchOption.class);

    private final String xmlId4Heteronym;

    public HeteronymSearchOption(HeteronymSearchOptionDto heteronymSearchOptionDto) {
        this.xmlId4Heteronym = heteronymSearchOptionDto.getXmlId4Heteronym();
    }

    @Override
    public Stream<SearchableElementDto> search(Stream<SearchableElementDto> inters) {
        return inters.filter(i -> verifiesSearchOption(i));
    }

    public boolean verifiesSearchOption(SearchableElementDto inter) {
        SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();

        if (ALL.equals(this.xmlId4Heteronym)) {
            // all are selected
            return true;
        } else if (this.xmlId4Heteronym == null && searchRequiresInterface.getHeteronymXmlId(inter.getXmlId()) == null) {
            // Searching for fragments with no authors and fragment has no
            // author
            return true;
        } else if ((this.xmlId4Heteronym != null && searchRequiresInterface.getHeteronymXmlId(inter.getXmlId()) == null)
                || (this.xmlId4Heteronym == null && searchRequiresInterface.getHeteronymXmlId(inter.getXmlId()) != null)) {
            // Searching for fragment with author and fragment has no author or
            // searching for fragment with no author and fragment has author and
            return false;
        } else {
            // the interpretation has the expected correct heteronym assignment
            return this.xmlId4Heteronym.equals(searchRequiresInterface.getHeteronymXmlId(inter.getXmlId()));
        }
    }

    public boolean hasHeteronym() {
        return this.xmlId4Heteronym != null && !this.xmlId4Heteronym.equals(ALL);
    }
}
