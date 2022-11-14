package pt.ist.socialsoftware.edition.ldod.bff.search.dto;

public class AdvancedTaxonomiesDto {
    private String taxonomies;

    public AdvancedTaxonomiesDto() {
    }

    public String getTaxonomies() {
        return taxonomies;
    }

    public void setTaxonomies(String taxonomies) {
        this.taxonomies = taxonomies;
    }

    @Override
    public String toString() {
        return "AdvancedTaxonomiesDto{" +
                "taxonomies='" + taxonomies + '\'' +
                '}';
    }
}
