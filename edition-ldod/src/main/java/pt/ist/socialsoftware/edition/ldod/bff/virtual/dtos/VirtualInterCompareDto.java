package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import java.util.List;

public class VirtualIntersCompareDto {

    private  List <TagDto> tags;
    private List<AnnotationDto> annotations;

    public VirtualIntersCompareDto(List <TagDto> tags, List<AnnotationDto> annotations) {
        this.tags = tags;
        this.annotations = annotations;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    public List<AnnotationDto> getAnnotations() {
        return annotations;
    }

    public void setAnnotations( List<AnnotationDto> annotations) {
        this.annotations = annotations;
    }
}
