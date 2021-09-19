package pt.ist.socialsoftware.edition.notification.dtos.text;


public class ExpertEditionInterListDto {

    private String title;
    private String acronym;
    private String type;
    private boolean pub;
    private int numberOfInters;



    public ExpertEditionInterListDto() {
        super();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public boolean isPub() {
        return this.pub;
    }

    public void setPub(boolean pub) {
        this.pub = pub;
    }

    public int getNumberOfInters() {
        return this.numberOfInters;
    }

    public void setNumberOfInters(int numberOfInters) {
        this.numberOfInters = numberOfInters;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
