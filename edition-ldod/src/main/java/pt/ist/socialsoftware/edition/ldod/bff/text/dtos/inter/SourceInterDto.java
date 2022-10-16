package pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter;

import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.SurfaceDto;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;

import java.util.List;

public class SourceInterDto extends FragInterDto {

    private String sourceType;
    private String heteronym;
    private List<SurfaceDto> surfaceDetailsList;
    private String interTitle;

    public SourceInterDto(FragInter inter) {
        super(inter);
    }

    public SourceInterDto(SourceInter inter, List<SurfaceDto> surfaceDtoList) {
        this(inter);
        setHeteronym(inter.getSource());
        setShortName(inter.getSource().getName());
        setSourceType(inter.getSource().getType().getDesc());
        setSurfaceDetailsList(surfaceDtoList);
    }

    public String getInterTitle() {
        return interTitle;
    }

    public void setInterTitle(String interTitle) {
        this.interTitle = interTitle;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
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


    public static final class SourceInterDtoBuilder {
        private final SourceInterDto sourceInterDto;

        private SourceInterDtoBuilder(FragInter inter) {
            sourceInterDto = new SourceInterDto(inter);
        }

        public static SourceInterDtoBuilder aSourceInterDto(FragInter inter) {
            return new SourceInterDtoBuilder(inter);
        }

        public SourceInterDtoBuilder title(String title) {
            sourceInterDto.setTitle(title);
            return this;
        }

        public SourceInterDtoBuilder interTitle(String title) {
            sourceInterDto.setInterTitle(title);
            return this;
        }

        public SourceInterDtoBuilder shortName(String shortName) {
            sourceInterDto.setShortName(shortName);
            return this;
        }

        public SourceInterDto build() {
            return sourceInterDto;
        }
    }
}
