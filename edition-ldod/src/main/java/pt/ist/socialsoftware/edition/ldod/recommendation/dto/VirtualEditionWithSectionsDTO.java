package pt.ist.socialsoftware.edition.ldod.recommendation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
        return acronym;
    }

    public List<SectionDTO> getSections() {
        return sections;
    }

}
