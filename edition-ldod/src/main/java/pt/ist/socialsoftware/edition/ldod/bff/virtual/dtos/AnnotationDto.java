package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.Annotation;
import pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation;
import pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation;

import java.util.List;
import java.util.stream.Collectors;

public class AnnotationDto {

    private boolean human;
    private String quote;
    private String text;
    private String sourceLink;
    private String profileURL;
    private String country;

    private String date;
    private String username;

    private List<TagDto> tags;


    public AnnotationDto(Annotation annotation) {
        setHuman(annotation.isHumanAnnotation());
        setQuote(annotation.getQuote());
        setUsername(annotation.getUser().getUsername());
        if (isHuman()) {
            setText(annotation.getText());
            setTags(((HumanAnnotation) annotation).getTagSet().stream().map(TagDto::new).collect(Collectors.toList()));
        } else {
            setDate(((AwareAnnotation) annotation).getDate());
            setSourceLink(((AwareAnnotation) annotation).getSourceLink());
            setProfileURL(((AwareAnnotation) annotation).getProfileURL());
            setCountry(((AwareAnnotation) annotation).getCountry());
        }
    }

    public boolean isHuman() {
        return human;
    }

    public void setHuman(boolean human) {
        this.human = human;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}
