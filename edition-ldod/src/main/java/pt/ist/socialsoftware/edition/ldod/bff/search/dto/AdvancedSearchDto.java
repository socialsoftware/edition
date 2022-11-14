package pt.ist.socialsoftware.edition.ldod.bff.search.dto;

import java.util.List;

public class AdvancedSearchDto {

    private List<AdvancedEditionDto> editions;
    private List<AdvancedHeteronymDto> heteronyms;
    private AdvancedPublicationDto publicationDates;
    private AdvancedManDto manuscriptDates;
    private AdvancedTypeDto typescriptDates;
    private List<AdvancedVeDto> virtualEditions;
    private AdvancedDatesDto dates;


    public AdvancedSearchDto(List<AdvancedEditionDto> editions,
                             List<AdvancedHeteronymDto> heteronyms,
                             AdvancedPublicationDto publicationsDates,
                             AdvancedDatesDto dates,
                             AdvancedManDto manCriteria,
                             AdvancedTypeDto typescriptDates,
                             List<AdvancedVeDto> virtualEditions
    ) {
        setEditions(editions);
        setHeteronyms(heteronyms);
        setPublicationDates(publicationsDates);
        setDates(dates);
        setManuscriptDates(manCriteria);
        setTypescriptDates(typescriptDates);
        setVirtualEditions(virtualEditions);
    }

    public List<AdvancedEditionDto> getEditions() {
        return editions;
    }

    public void setEditions(List<AdvancedEditionDto> editions) {
        this.editions = editions;
    }

    public List<AdvancedHeteronymDto> getHeteronyms() {
        return heteronyms;
    }

    public void setHeteronyms(List<AdvancedHeteronymDto> heteronyms) {
        this.heteronyms = heteronyms;
    }

    public AdvancedPublicationDto getPublicationDates() {
        return publicationDates;
    }

    public void setPublicationDates(AdvancedPublicationDto publicationDates) {
        this.publicationDates = publicationDates;
    }

    public AdvancedManDto getManuscriptDates() {
        return manuscriptDates;
    }

    public void setManuscriptDates(AdvancedManDto manuscriptDates) {
        this.manuscriptDates = manuscriptDates;
    }

    public AdvancedTypeDto getTypescriptDates() {
        return typescriptDates;
    }

    public void setTypescriptDates(AdvancedTypeDto typescriptDates) {
        this.typescriptDates = typescriptDates;
    }

    public List<AdvancedVeDto> getVirtualEditions() {
        return virtualEditions;
    }

    public void setVirtualEditions(List<AdvancedVeDto> virtualEditions) {
        this.virtualEditions = virtualEditions;
    }

    public AdvancedDatesDto getDates() {
        return dates;
    }

    public void setDates(AdvancedDatesDto dates) {
        this.dates = dates;
    }
}
