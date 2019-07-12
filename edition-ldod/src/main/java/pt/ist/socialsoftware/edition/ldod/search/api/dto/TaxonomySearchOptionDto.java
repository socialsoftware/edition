package pt.ist.socialsoftware.edition.ldod.search.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.TaxonomySearchOption;

public class TaxonomySearchOptionDto extends SearchOptionDto {
    private final String[] tags;

    public TaxonomySearchOptionDto(@JsonProperty("tags") String tags) {
        this.tags = tags.trim().split("\\s+");
    }


    @Override
    public TaxonomySearchOption createSearchOption() {
        return new TaxonomySearchOption(this);
    }

    public String[] getTags() {
        return this.tags;
    }
}
