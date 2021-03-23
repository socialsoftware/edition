package pt.ist.socialsoftware.edition.search.api.dto;

public class DatesForSearchDto {
    private String beginDate;
    private String endDate;

    public String getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(int i) {
        this.beginDate = Integer.toString(i);
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(int i) {
        this.endDate = Integer.toString(i);
    }
}
