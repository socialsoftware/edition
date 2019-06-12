package pt.ist.socialsoftware.edition.ldod.api.text.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Heteronym;

public class HeteronymDto {
    private String name;

    public HeteronymDto(Heteronym heteronym) {
        setName(heteronym.getName());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
