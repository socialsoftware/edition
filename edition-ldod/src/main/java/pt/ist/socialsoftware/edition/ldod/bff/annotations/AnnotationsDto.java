package pt.ist.socialsoftware.edition.ldod.bff.annotations;

import java.util.List;

public class AnnotationsDto {

    private List<AnnotationDto> annotations;

    private List<String> categories;
    private boolean openVocab;

    private boolean contributor;

    public AnnotationsDto(List<AnnotationDto> annotations, List<String> categories, boolean openVocab, boolean contributor) {
        setAnnotations(annotations);
        setOpenVocab(openVocab);
        setCategories(categories);
        setContributor(contributor);

    }

    public boolean isContributor() {
        return contributor;
    }

    public void setContributor(boolean contributor) {
        this.contributor = contributor;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<AnnotationDto> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationDto> annotations) {
        this.annotations = annotations;
    }

    public boolean getOpenVocab() {
        return openVocab;
    }

    public void setOpenVocab(boolean openVocab) {
        this.openVocab = openVocab;
    }
}
