package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.fragment;

import pt.ist.socialsoftware.edition.ldod.bff.annotations.AnnotationDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.TagDto;

import java.util.List;

public class VirtualFragmentInterCompareDto {

    private String acronym;
    private  List <TagDto> tags;
    private List<AnnotationDto> annotations;

    public VirtualFragmentInterCompareDto(String acronym, List <TagDto> tags, List<AnnotationDto> annotations) {
        setAcronym(acronym);
        setTags(tags);
        setAnnotations(annotations);
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
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
