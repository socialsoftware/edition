package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

import java.util.List;

public class VirtualFragmentInterDto {

    private List<VirtualEditionInterDto> inters;
    private List<TaxonomyDto> taxonomies;

    public VirtualFragmentInterDto() {
    }

    public List<VirtualEditionInterDto> getInters() {
        return inters;
    }

    public void setInters(List<VirtualEditionInterDto> inters) {
        this.inters = inters;
    }

    public List<TaxonomyDto> getTaxonomies() {
        return taxonomies;
    }

    public void setTaxonomies(List<TaxonomyDto> taxonomies) {
        this.taxonomies = taxonomies;
    }


    public static final class VirtualFragmentInterDtoBuilder {
        private final VirtualFragmentInterDto virtualFragmentInterDto;

        private VirtualFragmentInterDtoBuilder() {
            virtualFragmentInterDto = new VirtualFragmentInterDto();
        }

        public static VirtualFragmentInterDtoBuilder aVirtualFragmentInterDto() {
            return new VirtualFragmentInterDtoBuilder();
        }

        public VirtualFragmentInterDtoBuilder inters(List<VirtualEditionInterDto> inters) {
            virtualFragmentInterDto.setInters(inters);
            return this;
        }

        public VirtualFragmentInterDtoBuilder taxonomies(List<TaxonomyDto> taxonomies) {
            virtualFragmentInterDto.setTaxonomies(taxonomies);
            return this;
        }

        public VirtualFragmentInterDto build() {
            return virtualFragmentInterDto;
        }
    }
}
