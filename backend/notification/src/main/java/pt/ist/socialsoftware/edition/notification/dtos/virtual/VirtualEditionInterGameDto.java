package pt.ist.socialsoftware.edition.notification.dtos.virtual;



public class VirtualEditionInterGameDto {
    private String fragmentId;
    private String title;
    private int number;
    private String urlId;
    private String text;

    public VirtualEditionInterGameDto() {
    }

    public VirtualEditionInterGameDto(VirtualEditionInterDto inter) {
        this.setFragmentId(inter.getFragmentXmlId());
        this.title = inter.getTitle();
        this.number = inter.getNumber();
        this.urlId = inter.getUrlId();

//        this.text = inter.getLastUsed().getTranscription();
    }

    public String getFragmentId() {
        return this.fragmentId;
    }

    public void setFragmentId(String fragmentId) {
        this.fragmentId = fragmentId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUrlId() {
        return this.urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
