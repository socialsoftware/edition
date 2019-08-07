package pt.ist.socialsoftware.edition.ldod.virtual.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation;
import pt.ist.socialsoftware.edition.ldod.domain.Tag;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

import java.util.ArrayList;
import java.util.List;

public class HumanAnnotationDto extends AnnotationDto {

    private String text;
    private List<TagDto> tags;

    public HumanAnnotationDto(HumanAnnotation annotation, VirtualEditionInter inter) {
        super(annotation);

        setText(annotation.getText());
        List<TagDto> tags = new ArrayList<>();
        for (Tag tag : annotation.getTagSet()) {
            tags.add(new TagDto(tag, inter));
        }
        setTags(tags);

    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TagDto> getTags() {
        return this.tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    @Override
    public boolean isHumanAnnotation() {
        return true;
    }
}
