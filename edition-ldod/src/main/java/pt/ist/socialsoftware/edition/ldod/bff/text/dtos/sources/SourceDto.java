package pt.ist.socialsoftware.edition.ldod.bff.text.dtos.sources;

import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.SourceInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDDate;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.domain.Surface_Base;

import java.util.List;
import java.util.stream.Collectors;

public class SourceDto {

    private String externalId;

    private String title;
    private String name;
    private List<SourceInterDto> sourceIntersList;
    private String date = null;
    private String sourceType;
    private String altIdentifier;
    private String heteronym;
    private List<String> surfaceList;


    public SourceDto(Source source, List<SourceInterDto> sourceIntersList) {
        setTitle(source.getFragment().getTitle());
        setExternalId(source.getExternalId());
        setName(source.getName());
        setSourceIntersList(sourceIntersList);
        setDate(source.getLdoDDate());
        setSourceType(source.getType().getDesc());
        setHeteronym(source);
        setSurfaceList(source);
        setAltIdentifier(source.getAltIdentifier());
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }


    public void setHeteronym(Source source) {
        this.heteronym = source.getHeteronym().isNullHeteronym()
                ? null
                : source.getHeteronym().getName();
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setDate(LdoDDate date) {
        if (date != null) {
            this.date = date.print();
        }
    }


    public void setSurfaceList(Source source) {
        if (source.getFacsimile() != null)
            this.surfaceList = source.getFacsimile().getSurfaces()
                    .stream().map(Surface_Base::getGraphic)
                    .collect(Collectors.toList());
    }

    public List<SourceInterDto> getSourceIntersList() {
        return sourceIntersList;
    }

    public void setSourceIntersList(List<SourceInterDto> sourceIntersList) {
        this.sourceIntersList = sourceIntersList;
    }

    public String getHeteronym() {
        return heteronym;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public List<String> getSurfaceList() {
        return surfaceList;
    }

    public String getAltIdentifier() {
        return altIdentifier;
    }

    public void setAltIdentifier(String altIdentifier) {
        this.altIdentifier = altIdentifier;
    }


}
