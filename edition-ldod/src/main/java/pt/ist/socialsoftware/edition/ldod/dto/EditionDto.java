package pt.ist.socialsoftware.edition.ldod.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ScholarEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

public class EditionDto {
    private String acronym;

    public EditionDto(ScholarEdition edition) {
        this.acronym = edition.getAcronym();
    }

    public EditionDto(VirtualEdition edition) {
        this.acronym = edition.getAcronym();
    }

    public String getAcronym() {
        return this.acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }
}
