package pt.ist.socialsoftware.edition.virtual.api.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.domain.Annotation;
import pt.ist.socialsoftware.edition.virtual.domain.Range;

import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({@JsonSubTypes.Type(value = AwareAnnotationDto.class, name = AnnotationDto.AWARE),
        @JsonSubTypes.Type(value = HumanAnnotationDto.class, name = AnnotationDto.HUMAN)})
public abstract class AnnotationDto {

    public static final String AWARE = "aware";
    public static final String HUMAN = "human";

    private String quote;
    private String username;
    private String text;
    private String externalId;
    private String interExternalId;
    private String interXmlId;
    private String user;

    public AnnotationDto(Annotation annotation) {
        setQuote(annotation.getQuote());
        setUsername(annotation.getUser());
        setText(annotation.getText());
        this.externalId = annotation.getExternalId();
        this.interExternalId = annotation.getVirtualEditionInter().getExternalId();
        this.interXmlId = annotation.getVirtualEditionInter().getXmlId();
        this.user = annotation.getUser();
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getInterExternalId() {
        return interExternalId;
    }

    public String getUser() {
        return user;
    }

    public abstract boolean isHumanAnnotation();

    public String getInterXmlId() {
        return interXmlId;
    }
}
