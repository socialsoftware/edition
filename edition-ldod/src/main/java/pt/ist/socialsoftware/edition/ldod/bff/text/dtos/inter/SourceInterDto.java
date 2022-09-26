package pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter;

import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.SurfaceDto;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;

import java.util.List;

public abstract class SourceInterDto extends FragInterDto {

    private String sourceType;

    private String heteronym;
    private String shortName;
    private List<SurfaceDto> surfaceDetailsList;

    public SourceInterDto(SourceInter inter) {
        super(inter);
    }

    public SourceInterDto(SourceInter inter, List<SurfaceDto> surfaceDtoList) {
        this(inter);
        setHeteronym(inter.getSource());
        setShortName(inter.getSource().getName());
        setSourceType(inter.getSource().getType().getDesc());
        setSurfaceDetailsList(surfaceDtoList);
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    public String getHeteronym() {
        return heteronym;
    }

    public void setHeteronym(Source source) {
        this.heteronym = source.getHeteronym().isNullHeteronym()
                ? null
                : source.getHeteronym().getName();
    }

    public List<SurfaceDto> getSurfaceDetailsList() {
        return surfaceDetailsList;
    }

    public void setSurfaceDetailsList(List<SurfaceDto> surfaceDetailsList) {
        this.surfaceDetailsList = surfaceDetailsList;
    }

}
