package pt.ist.socialsoftware.edition.ldod.bff.search.dto;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

public class AdvancedVeDto {
    private String veAcronym;
    private String title;

    private boolean veInclusion;

    public AdvancedVeDto() {
    }

    public AdvancedVeDto(VirtualEdition ve) {
        setVeAcronym(ve.getAcronym());
        setTitle(ve.getTitle());
    }

    public String getVeAcronym() {
        return veAcronym;
    }

    public void setVeAcronym(String veAcronym) {
        this.veAcronym = veAcronym;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "AdvancedVeDto{" +
                "acronym='" + veAcronym + '\'' +
                ", title='" + title + '\'' +
                ", inclusion=" + veInclusion +
                '}';
    }
}
