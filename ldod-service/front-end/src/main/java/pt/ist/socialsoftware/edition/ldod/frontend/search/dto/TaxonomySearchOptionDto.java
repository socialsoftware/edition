package pt.ist.socialsoftware.edition.ldod.frontend.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaxonomySearchOptionDto extends SearchOptionDto {
    private final String[] tags;

    public TaxonomySearchOptionDto(@JsonProperty("tags") String tags) {
        this.tags = tags.trim().split("\\s+");
    }


    public String[] getTags() {
        return this.tags;
    }
}
