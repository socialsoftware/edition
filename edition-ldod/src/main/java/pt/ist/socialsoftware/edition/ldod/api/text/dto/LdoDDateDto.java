package pt.ist.socialsoftware.edition.ldod.api.text.dto;

import org.joda.time.LocalDate;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDDate;

public class LdoDDateDto {
    private LdoDDate.DateType type;
    private LocalDate date;

    public LdoDDateDto(LdoDDate ldoDDate) {
        setType(ldoDDate.getType());
        setDate(ldoDDate.getDate());
    }

    public String print() {
        switch (getType()) {
            case YEAR:
                return getDate().toString("yyyy");
            case DAY:
                return getDate().toString("dd-MM-yyyy");
            case MONTH:
                return getDate().toString("MM-yyyy");
            default:
                return "";
        }
    }

    public LdoDDate.DateType getType() {
        return this.type;
    }

    public void setType(LdoDDate.DateType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


}
