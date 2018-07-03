package pt.ist.socialsoftware.edition.ldod.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Taxonomy;


public class TaxonomyDTO {
    private boolean openManagement;
    private boolean openVocabulary;
    private boolean openAnnotation;

    public TaxonomyDTO() {
    }

    public TaxonomyDTO(Taxonomy taxonomy) {
        this.setOpenManagement(taxonomy.getOpenManagement());
        this.setOpenVocabulary(taxonomy.getOpenVocabulary());
        this.setOpenAnnotation(taxonomy.getOpenAnnotation());
    }

    public boolean isOpenManagement() {
        return openManagement;
    }

    public void setOpenManagement(boolean openManagement) {
        this.openManagement = openManagement;
    }

    public boolean isOpenVocabulary() {
        return openVocabulary;
    }

    public void setOpenVocabulary(boolean openVocabulary) {
        this.openVocabulary = openVocabulary;
    }

    public boolean isOpenAnnotation() {
        return openAnnotation;
    }

    public void setOpenAnnotation(boolean openAnnotation) {
        this.openAnnotation = openAnnotation;
    }

}
