package pt.ist.socialsoftware.edition.ldod.search.feature.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.search.feature.SearchableElement;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.LdoDDateDto;

import java.util.stream.Stream;

public final class DateSearchOption extends SearchOption {
    private static final Logger logger = LoggerFactory.getLogger(DateSearchOption.class);

    enum Dated {
        ALL("all"), DATED("dated"), UNDATED("undated");

        private final String dated;

        private Dated(String dated) {
            this.dated = dated;
        }

        public String getDated() {
            return this.dated;
        }
    }

    private final Dated dated;
    private int begin;
    private int end;

    public DateSearchOption(@JsonProperty("option") String dated, @JsonProperty("begin") String begin,
                            @JsonProperty("end") String end) {
        logger.debug("DateSearchOption dated: {}, begin:{}, end:{}", dated, begin, end);

        if (Dated.DATED.getDated().equals(dated)) {
            this.dated = Dated.DATED;
            this.begin = begin == null ? 1911 : Integer.parseInt(begin);
            this.end = end == null ? 1935 : Integer.parseInt(end);
        } else if (Dated.UNDATED.getDated().equals(dated)) {
            this.dated = Dated.UNDATED;
        } else {
            this.dated = Dated.ALL;
        }

        logger.debug("DateSearchOption dated: {}, begin:{}, end:{}", this.dated, this.begin, this.end);

    }

    @Override
    public String toString() {
        return "Date: dated:" + this.dated.getDated() + " begin:" + this.begin + " end:" + this.end;
    }

    @Override
    public Stream<SearchableElement> search(Stream<SearchableElement> inters) {
        return inters.filter(inter -> inter.getType() == SearchableElement.Type.SCHOLAR_INTER)
                .filter(i -> verifiesSearchOption(i));
    }

    public boolean verifiesSearchOption(SearchableElement inter) {
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
