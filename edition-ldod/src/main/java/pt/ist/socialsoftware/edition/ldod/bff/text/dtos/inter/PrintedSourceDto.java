package pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter;

import pt.ist.socialsoftware.edition.ldod.domain.PrintedSource;
import pt.ist.socialsoftware.edition.ldod.domain.Source;

public class PrintedSourceDto extends SourceInterDto {
    private final Source.SourceType sourceType = Source.SourceType.PRINTED;
    private String journal;
    private String number;
    private Integer startPage;
    private Integer endPage;
    private String pubPlace;

    public PrintedSourceDto(PrintedSource source) {
        super(source);
        setNumber(source.getIssue());
        setJournal(source.getJournal());
        setStartPage(source.getStartPage());
        setEndPage(source.getEndPage());
        setPubPlace(source.getPubPlace());
    }

    public Source.SourceType getSourceType() {
        return sourceType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
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

    public String getPubPlace() {
        return pubPlace;
    }

    public void setPubPlace(String pubPlace) {
        this.pubPlace = pubPlace;
    }
}
