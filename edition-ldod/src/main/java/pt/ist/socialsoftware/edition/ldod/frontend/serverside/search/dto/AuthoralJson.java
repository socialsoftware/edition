package pt.ist.socialsoftware.edition.ldod.frontend.serverside.search.dto;


public class AuthoralJson {

    private String[] mediums;
    private DatesJson dates;

    public String[] getMediums() {
        return this.mediums;
    }

    public void setMediums(String[] mediums) {
        this.mediums = mediums;
    }

    public DatesJson getDates() {
        return this.dates;
    }

    public void setDates(DatesJson dates) {
        this.dates = dates;
    }
}
