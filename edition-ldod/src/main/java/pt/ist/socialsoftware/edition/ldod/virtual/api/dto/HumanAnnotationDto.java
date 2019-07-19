package pt.ist.socialsoftware.edition.ldod.virtual.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation;
import pt.ist.socialsoftware.edition.ldod.domain.Tag;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

import java.util.ArrayList;
import java.util.List;

public class HumanAnnotationDto {

    private String quote;
    private String username;
    private String text;
    private List<TagDto> tags;

    public HumanAnnotationDto(HumanAnnotation annotation, VirtualEditionInter inter){
        setQuote(annotation.getQuote());
        setUsername(annotation.getUser());
        setText(annotation.getText());
        List<TagDto> tags = new ArrayList<>();
        for(Tag tag : annotation.getTagSet()){
            tags.add(new TagDto(tag,inter));
        }
        setTags(tags);

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }
}
