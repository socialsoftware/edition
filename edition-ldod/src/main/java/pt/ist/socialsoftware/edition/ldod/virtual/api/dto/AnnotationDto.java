package pt.ist.socialsoftware.edition.ldod.virtual.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Annotation;

public class AnnotationDto {

    private String quote;
    private String username;

    public AnnotationDto(Annotation annotation) {
        setQuote(annotation.getQuote());
        setUsername(annotation.getUser());
    }

    public String getQuote() {
        return this.quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
