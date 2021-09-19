package pt.ist.socialsoftware.edition.notification.dtos.virtual;


public class AwareAnnotationDto extends AnnotationDto {

    private String source;
    private String profile;
    private String date;
    private String country;

    public AwareAnnotationDto() {}

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean isHumanAnnotation() {
        return false;
    }
}
