package pt.ist.socialsoftware.edition.ldod.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Edition;

public class EditionDto {

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    private String acronym;

    public EditionDto(Edition edition) {
        this.acronym = edition.getAcronym();
    }
}
