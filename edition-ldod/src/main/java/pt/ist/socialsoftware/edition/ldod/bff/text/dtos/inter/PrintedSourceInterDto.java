package pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter;

import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.SurfaceDto;
import pt.ist.socialsoftware.edition.ldod.domain.PbText;
import pt.ist.socialsoftware.edition.ldod.domain.PrintedSource;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;

import java.util.List;

public class PrintedSourceInterDto extends SourceInterDto {
    private String journal;
    private Integer startPage;
    private Integer endPage;
    private String pubPlace;

    public PrintedSourceInterDto(SourceInter inter) {
        super(inter);
    }

    public PrintedSourceInterDto(SourceInter inter, List<SurfaceDto> surfaceDtoList) {
        super(inter, surfaceDtoList);
        PrintedSource source = (PrintedSource) inter.getSource();
        setTitle(inter.getTitle());
        setJournal(source.getJournal());
        setStartPage(source.getStartPage());
        setEndPage(source.getEndPage());
        setPubPlace(source.getPubPlace());
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
