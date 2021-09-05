package pt.ist.socialsoftware.edition.notification.dtos.frontend;//package pt.ist.socialsoftware.edition.ldod.frontend.utils.dto;


import pt.ist.socialsoftware.edition.notification.dtos.text.ExpertEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;

public class EditionDto {
    private String acronym;

    public EditionDto(ExpertEditionDto edition) {
        this.acronym = edition.getAcronym();
    }

    public EditionDto(VirtualEditionDto edition) {
        this.acronym = edition.getAcronym();
    }

    public String getAcronym() {
        return this.acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }
}
