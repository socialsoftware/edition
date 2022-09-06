package pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDDate;

import java.util.List;
import java.util.stream.Collectors;

public class EditorialInterDto {

    private String title;
    private String acronym;
    private String heteronym;
    private String volume;
    private String number;
    private Integer startPage;
    private Integer endPage;
    private String date = null;
    private String precision;
    private String notes;
    private List<String> annexNote;


    public EditorialInterDto(ExpertEditionInter inter) {
        setTitle(inter.getTitle());
        setAcronym(inter.getEdition().getAcronym());
        setHeteronym(inter);
        setVolume(inter.getVolume());
        setNumber(inter.getCompleteNumber());
        setStartPage(inter.getStartPage());
        setEndPage(inter.getEndPage());
        setDate(inter.getLdoDDate());
        setNotes(inter.getNotes());
        setAnnexNote(inter);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getHeteronym() {
        return heteronym;
    }

    public void setHeteronym(ExpertEditionInter inter) {
        this.heteronym = inter.getHeteronym().isNullHeteronym()
                ? null
                : inter.getHeteronym().getName();
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getStartPage() {
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public Integer getEndPage() {
        return endPage;
    }

    public void setEndPage(Integer endPage) {
        this.endPage = endPage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(LdoDDate date) {
        if (date != null) {
            this.date = date.print();
            setPrecision(date);
        }
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(LdoDDate date) {
        this.precision = date.getPrecision() != null ? date.getPrecision().getDesc() : null;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getAnnexNote() {
        return annexNote;
    }

    public void setAnnexNote(ExpertEditionInter inter) {
        this.annexNote =
                inter.getSortedAnnexNote()
                        .stream()
                        .map(note -> note.getNoteText().generatePresentationText())
                        .collect(Collectors.toList());
    }
}
