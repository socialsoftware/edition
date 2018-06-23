package pt.ist.socialsoftware.edition.ldod.recommendation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SectionDTO {
    private final List<String> sections;
    private final String inter;

    public SectionDTO(@JsonProperty("depth") List<String> sections,
            @JsonProperty("id") String inter) {
        this.sections = sections;
        this.inter = inter;
    }

    public List<String> getSections() {
        return sections;
    }

    public String getInter() {
        return inter;
    }
}