package pt.ist.socialsoftware.edition.ldod.virtual.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation;

public class AwareAnnotationDto {

    private String quote;
    private String username;
    private String source;
    private String profile;
    private String date;
    private String country;

    public AwareAnnotationDto(AwareAnnotation annotation){
        setQuote(annotation.getQuote());
        setUsername(annotation.getUser());
        setSource(annotation.getSourceLink());
        setProfile(annotation.getProfileURL());
        setDate(annotation.getDate());
        setCountry(annotation.getCountry());
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
