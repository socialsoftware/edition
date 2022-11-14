package pt.ist.socialsoftware.edition.ldod.bff.search.dto;

public class AdvancedManDto {

    private Boolean markLdoD;
    private boolean dated;
    private String beginDate;
    private String endDate;

    public AdvancedManDto(String beginDate, String endDate) {
        setBeginDate(beginDate);
        setEndDate(endDate);
    }

    public AdvancedManDto() {
    }

    public Boolean getMarkLdoD() {
        return markLdoD;
    }

    public void setMarkLdoD(Boolean markLdoD) {
        this.markLdoD = markLdoD;
    }

    public boolean isDated() {
        return dated;
    }

    public void setDated(boolean dated) {
        this.dated = dated;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "AdvancedManDto{" +
                "markLdoD=" + markLdoD +
                ", dated=" + dated +
                ", beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
