package pt.ist.socialsoftware.edition.ldod.virtual.feature.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SectionDTO {
    private final List<String> sections;
    private final String inter;

    public SectionDTO(@JsonProperty("depth") List<String> sections,
                      @JsonProperty("id") String inter) {
        this.sections = sections;
        this.inter = inter;
    }

    public List<String> getSections() {
        return this.sections;
    }

    public String getInter() {
        return this.inter;
    }
}