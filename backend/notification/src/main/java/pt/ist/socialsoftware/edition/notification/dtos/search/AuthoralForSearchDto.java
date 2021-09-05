package pt.ist.socialsoftware.edition.notification.dtos.search;


public class AuthoralForSearchDto {

    private String[] mediums;
    private DatesForSearchDto dates;

    public String[] getMediums() {
        return this.mediums;
    }

    public void setMediums(String[] mediums) {
        this.mediums = mediums;
    }

    public DatesForSearchDto getDates() {
        return this.dates;
    }

    public void setDates(DatesForSearchDto dates) {
        this.dates = dates;
    }
}
