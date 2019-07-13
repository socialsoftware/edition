package pt.ist.socialsoftware.edition.ldod.virtual.feature.recommendation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CreateVirtualEditionWithSectionsDTO
        extends VirtualEditionWithSectionsDTO {

    private final String title;
    private final boolean pub;

    public CreateVirtualEditionWithSectionsDTO(
            @JsonProperty("acronym") String acronym,
            @JsonProperty("title") String title,
            @JsonProperty("pub") boolean pub,
            @JsonProperty("sections") List<SectionDTO> sections) {
        super(acronym, sections);
        this.title = title;
        this.pub = pub;
    }

    public boolean getPub() {
        return this.pub;
    }

    public String getTitle() {
        return this.title;
    }

}
