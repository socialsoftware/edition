package pt.ist.socialsoftware.edition.search.feature.options;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.notification.dtos.text.LdoDDateDto;
import pt.ist.socialsoftware.edition.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.search.api.dto.DateSearchOptionDto;
import pt.ist.socialsoftware.edition.search.api.dto.SearchableElementDto;

import java.util.stream.Stream;

public final class DateSearchOption extends SearchOption {
    private static final Logger logger = LoggerFactory.getLogger(DateSearchOption.class);

    public enum Dated {
        ALL("all"), DATED("dated"), UNDATED("undated");

        private final String dated;

        Dated(String dated) {
            this.dated = dated;
        }

        public String getDated() {
            return this.dated;
        }
    }

    private final Dated dated;
    private final int begin;
    private final int end;


    public DateSearchOption(DateSearchOptionDto dateSearchOptionDto) {
        this.dated = dateSearchOptionDto.getDated();
        this.begin = dateSearchOptionDto.getBegin();
        this.end = dateSearchOptionDto.getEnd();
    }

    @Override
    public Stream<SearchableElementDto> search(Stream<SearchableElementDto> inters) {
        return inters.filter(inter -> inter.getType() == SearchableElementDto.Type.SCHOLAR_INTER)
                .filter(i -> verifiesSearchOption(i));
    }

    public boolean verifiesSearchOption(SearchableElementDto inter) {
        SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();

        if (this.dated != Dated.ALL) {
            return isInDate(searchRequiresInterface.getScholarInterDate(inter.getXmlId()));
        }
        return true;
    }

    public boolean isInDate(LdoDDateDto ldoDDate) {
        if (ldoDDate == null && this.dated.equals(Dated.UNDATED)) {
            return true;
        } else if ((ldoDDate == null && this.dated.equals(Dated.DATED))
                || (ldoDDate != null && this.dated.equals(Dated.UNDATED))) {
            return false;
        } else if (this.dated.equals(Dated.DATED)
                && (this.begin > ldoDDate.getDate().getYear() || this.end < ldoDDate.getDate().getYear())) {
            return false;
        } else {
            return true;
        }
    }

    public boolean hasDate() {
        return !this.dated.equals(Dated.ALL);
    }
}
