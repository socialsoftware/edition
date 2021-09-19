package pt.ist.socialsoftware.edition.notification.dtos.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.notification.enums.Dated;


public final class DateSearchOptionDto extends SearchOptionDto {
    private static final Logger logger = LoggerFactory.getLogger(DateSearchOptionDto.class);


    private final Dated dated;
    private int begin;
    private int end;

    public DateSearchOptionDto(@JsonProperty("option") String dated, @JsonProperty("begin") String begin,
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

    public Dated getDated() {
        return this.dated;
    }

    public int getBegin() {
        return this.begin;
    }

    public int getEnd() {
        return this.end;
    }
}
