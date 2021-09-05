package pt.ist.socialsoftware.edition.search.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.search.feature.options.TaxonomySearchOption;

public class TaxonomySearchOptionDto extends SearchOptionDto {
    private final String[] tags;

    public TaxonomySearchOptionDto(@JsonProperty("tags") String[] tags) {
        this.tags = tags;
    }


    @Override
    public TaxonomySearchOption createSearchOption() {
        return new TaxonomySearchOption(this);
    }

    public String[] getTags() {
        return this.tags;
    }
}
