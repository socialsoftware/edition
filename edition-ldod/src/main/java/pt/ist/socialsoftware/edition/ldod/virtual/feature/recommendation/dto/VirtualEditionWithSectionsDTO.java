package pt.ist.socialsoftware.edition.ldod.virtual.feature.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class VirtualEditionWithSectionsDTO {
    private final String acronym;
    private final List<SectionDTO> sections;

    public VirtualEditionWithSectionsDTO(
            @JsonProperty("acronym") String acronym,
            @JsonProperty("sections") List<SectionDTO> sections) {
        this.acronym = acronym;
        this.sections = sections;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public List<SectionDTO> getSections() {
        return this.sections;
    }

}
