package pt.ist.socialsoftware.edition.search.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.search.feature.options.DateSearchOption;


public final class DateSearchOptionDto extends SearchOptionDto {
    private static final Logger logger = LoggerFactory.getLogger(DateSearchOptionDto.class);


    private final DateSearchOption.Dated dated;
    private int begin;
    private int end;

    public DateSearchOptionDto(@JsonProperty("option") String dated, @JsonProperty("begin") String begin,
                               @JsonProperty("end") String end) {
        logger.debug("DateSearchOption dated: {}, begin:{}, end:{}", dated, begin, end);

        if (DateSearchOption.Dated.DATED.getDated().equals(dated)) {
            this.dated = DateSearchOption.Dated.DATED;
            this.begin = begin == null ? 1911 : Integer.parseInt(begin);
            this.end = end == null ? 1935 : Integer.parseInt(end);
        } else if (DateSearchOption.Dated.UNDATED.getDated().equals(dated)) {
            this.dated = DateSearchOption.Dated.UNDATED;
        } else {
            this.dated = DateSearchOption.Dated.ALL;
        }

        logger.debug("DateSearchOption dated: {}, begin:{}, end:{}", this.dated, this.begin, this.end);

    }

    @Override
    public DateSearchOption createSearchOption() {
        return new DateSearchOption(this);
    }

    public DateSearchOption.Dated getDated() {
        return this.dated;
    }

    public int getBegin() {
        return this.begin;
    }

    public int getEnd() {
        return this.end;
    }
}
