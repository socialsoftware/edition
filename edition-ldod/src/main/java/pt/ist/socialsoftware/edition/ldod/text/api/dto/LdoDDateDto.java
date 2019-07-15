package pt.ist.socialsoftware.edition.ldod.text.api.dto;

import org.joda.time.LocalDate;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDDate;

public class LdoDDateDto {
    private LdoDDate.DateType type;
    private LocalDate date;
    private Fragment.PrecisionType precisionType;

    public LdoDDateDto(LdoDDate ldoDDate) {
        setType(ldoDDate.getType());
        setDate(ldoDDate.getDate());
        setPrecision(ldoDDate.getPrecision());
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

    public Fragment.PrecisionType getPrecision() {
        return precisionType;
    }

    public void setPrecision(Fragment.PrecisionType precisionType) {
        this.precisionType = precisionType;
    }
}
