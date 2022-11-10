package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.fragment;

import pt.ist.socialsoftware.edition.ldod.bff.annotations.AnnotationDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.CategoryDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.TaxonomyDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.VirtualEditionInterDto;

import java.util.List;

public class VirtualFragmentInterDto {

    private VirtualEditionInterDto inter;
    private TaxonomyDto taxonomy;
    private List<AnnotationDto> annotations;

    private boolean veOpenVocab;

    private List<CategoryDto> categoryDtos;

    public VirtualFragmentInterDto() {
    }

    public VirtualEditionInterDto getInter() {
        return inter;
    }

    public void setInter(VirtualEditionInterDto inter) {
        this.inter = inter;
    }

    public TaxonomyDto getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(TaxonomyDto taxonomy) {
        this.taxonomy = taxonomy;
    }

    public List<AnnotationDto> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationDto> annotations) {
        this.annotations = annotations;
    }

    public static final class VirtualFragmentInterDtoBuilder {
        private final VirtualFragmentInterDto virtualFragmentInterDto;

        private VirtualFragmentInterDtoBuilder() {
            virtualFragmentInterDto = new VirtualFragmentInterDto();
        }

        public static VirtualFragmentInterDtoBuilder aVirtualFragmentInterDto() {
            return new VirtualFragmentInterDtoBuilder();
        }

        public VirtualFragmentInterDtoBuilder inter(VirtualEditionInterDto inter) {
            virtualFragmentInterDto.setInter(inter);
            return this;
        }

        public VirtualFragmentInterDtoBuilder taxonomy(TaxonomyDto taxonomy) {
            virtualFragmentInterDto.setTaxonomy(taxonomy);
            return this;
        }

        public VirtualFragmentInterDtoBuilder annotations(List<AnnotationDto> annotations) {
            virtualFragmentInterDto.setAnnotations(annotations);
            return this;
        }

        public VirtualFragmentInterDto build() {
            return virtualFragmentInterDto;
        }
    }
}
