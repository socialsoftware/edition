package pt.ist.socialsoftware.edition.ldod.bff.text.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDDate;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.domain.Surface_Base;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SourceDto {

    private String externalId;
    private String name;
    private String altIdentifier;
    private String heteronym;
    private String xmlId;
    private String urlId;
    private String date = null;
    private String precision;
    private List<String> surfaceList;
    private Integer sourceIntersSetSize;

    public SourceDto(Source source) {
        setExternalId(source.getExternalId());
        setHeteronym(source);
        setName(source.getName());
        setXmlId(source);
        setUrlId(source);
        setDate(source.getLdoDDate());
        setSurfaceList(source);
        setAltIdentifier(source.getAltIdentifier());
        setSourceIntersSetSize(source.getSourceIntersSet().size());

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

    public void setXmlId(Source source) {
        this.xmlId = source.getSourceIntersSet()
                .stream()
                .map(sourceInter -> sourceInter.getFragment().getXmlId())
                .findFirst()
                .orElse(null);
    }

    public void setUrlId(Source source) {
        this.urlId = source.getSourceIntersSet()
                .stream()
                .map(FragInter::getUrlId)
                .findFirst()
                .orElse(null);
    }

    public void setDate(LdoDDate date) {
        if (date != null) {
            setPrecision(date);
            this.date = date.print();
        }
    }

    public void setPrecision(LdoDDate date) {
        this.precision = date.getPrecision() != null ? date.getPrecision().getDesc() : null;
    }

    public void setSurfaceList(Source source) {
        if (source.getFacsimile() != null)
            this.surfaceList = source.getFacsimile().getSurfaces()
                    .stream().map(Surface_Base::getGraphic)
                    .collect(Collectors.toList());
    }

    public String getHeteronym() {
        return heteronym;
    }

    public String getName() {
        return name;
    }

    public String getXmlId() {
        return xmlId;
    }

    public String getUrlId() {
        return urlId;
    }

    public String getDate() {
        return date;
    }

    public String getPrecision() {
        return precision;
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

    public Integer getSourceIntersSetSize() {
        return sourceIntersSetSize;
    }

    public void setSourceIntersSetSize(Integer sourceIntersSetSize) {
        this.sourceIntersSetSize = sourceIntersSetSize;
    }


}
