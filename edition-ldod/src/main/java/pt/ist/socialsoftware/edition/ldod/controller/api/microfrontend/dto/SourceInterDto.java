package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource.Form;
import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource.Material;
import pt.ist.socialsoftware.edition.ldod.domain.Source.SourceType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class SourceInterDto {
    private SourceType sourceType;
    private String title;
    private Form form;
    private Integer dimensionSetSize;
    private Material material;
    private Integer columns;
    private Boolean hadLdoDLabel;
    private String journal;
    private String issue;
    private int startPage;
    private int endPage;
    private String pubPlace;
    private String notes;
    private String altIdentifier;
    private String desc;
    private List<DimensionsDto> dimensionDtoList;
    private List<HandNoteDto> handNoteDtoSet;
    private List<TypeNoteDto> typeNoteSet;
    private List<SurfaceDto> surfaceDto;
    private String date;
    private List<SourceInterSimpleDto> sourceInterSet;
    private String shortName;
    private String externalId;
    private Optional<HandNoteDto> handNoteDto;
    private Optional<TypeNoteDto> typeNoteDto;
    private List<String> surfaceString;
    private String transcription;
    private String xmlId;
    private String urlId;
    private String heteronym;
    private boolean heteronymNull;


    public SourceInterDto(SourceInter sourceInter) {

        this.sourceType = sourceInter.getSource().getType();
        this.setShortName(sourceInter.getShortName());
        this.setExternalId(sourceInter.getExternalId());

        setHeteronym(sourceInter.getHeteronym().getName());
        setHeteronymNull(sourceInter.getHeteronym().isNullHeteronym());

        if (sourceInter.getLdoDDate() != null) {
            this.setDate(sourceInter.getLdoDDate().print());
            if (sourceInter.getLdoDDate().getPrecision() != null) {
                this.setDesc(sourceInter.getLdoDDate().getPrecision().getDesc());
            }
        }

        setXmlId(sourceInter.getSource().getSourceIntersSet().stream()
                .map(SourceInterSimpleDto::new)
                .map(SourceInterSimpleDto::getXmlId)
                .findFirst()
                .orElse(""));

        setUrlId(sourceInter.getSource().getSourceIntersSet().stream()
                .map(SourceInterSimpleDto::new)
                .map(SourceInterSimpleDto::getUrlId)
                .findFirst()
                .orElse(""));


        if (sourceInter.getSource().getType().equals(Source.SourceType.MANUSCRIPT)) {
            ManuscriptSource source = (ManuscriptSource) sourceInter.getSource();
            this.title = "";
            this.setForm(source.getForm());
            this.setDimensionSetSize(source.getDimensionsSet().size());

            this.setDimensionDtoList(source.getSortedDimensions().stream()
                    .map(DimensionsDto::new)
                    .collect(Collectors.toList()));

            this.setHandNoteDtoSet(source.getHandNoteSet().stream()
                    .map(HandNoteDto::new)
                    .collect(Collectors.toList()));

            this.setTypeNoteSet(source.getTypeNoteSet().stream()
                    .map(TypeNoteDto::new)
                    .collect(Collectors.toList()));

            setHandNoteDto(source.getHandNoteSet().stream().map(HandNoteDto::new).findFirst());
            setTypeNoteDto(source.getTypeNoteSet().stream().map(TypeNoteDto::new).findFirst());
            setTranscription(source.getFragment().getRepresentativeSourceInter().getTitle());


            this.setMaterial(source.getMaterial());
            this.setColumns(source.getColumns());
            this.setHadLdoDLabel(source.getHasLdoDLabel());
            this.setNotes(source.getNotes());

            if (source.getFacsimile() != null) {
                if (source.getFacsimile().getSurfaces() != null) {
                    this.setSurfaceDto(source.getFacsimile().getSurfaces().stream()
                            .map(SurfaceDto::new)
                            .collect(Collectors.toList()));
                    setSurfaceString(source.getFacsimile().getSurfaces().stream().map(Surface_Base::getGraphic).collect(Collectors.toList()));
                }
            }


            this.setAltIdentifier(source.getAltIdentifier());
        } else {
            PrintedSource source = (PrintedSource) sourceInter.getSource();
            setTitle(source.getTitle());
            setJournal(source.getJournal());
            setIssue(source.getIssue());
            setStartPage(source.getStartPage());
            setEndPage(source.getEndPage());
            setPubPlace(source.getPubPlace());
            setAltIdentifier(source.getAltIdentifier());


            if (source.getFacsimile() != null) {
                if (source.getFacsimile().getSurfaces() != null) {
                    this.setSurfaceDto(source.getFacsimile().getSurfaces().stream()
                            .map(SurfaceDto::new)
                            .collect(Collectors.toList()));
                    setSurfaceString(source.getFacsimile().getSurfaces().stream().map(Surface_Base::getGraphic).collect(Collectors.toList()));

                }
            }
        }
    }

    public SourceInterDto(Source source) {
        this.setSourceType(source.getType());
        this.setTitle(source.getName());
        if (source.getLdoDDate() != null) {
            this.setDate(source.getLdoDDate().print());
        }
        setExternalId(source.getExternalId());
        this.setSourceInterSet(source.getSourceIntersSet().stream()
                .map(SourceInterSimpleDto::new)
                .collect(Collectors.toList()));
        setHeteronym(source.getHeteronym().getName());
        setHeteronymNull(source.getHeteronym().isNullHeteronym());

        setTranscription(source.getFragment().getRepresentativeSourceInter().getTitle());
        setXmlId(source.getSourceIntersSet().stream()
                .map(SourceInterSimpleDto::new)
                .map(SourceInterSimpleDto::getXmlId)
                .findFirst()
                .orElse(""));

        setUrlId(source.getSourceIntersSet().stream()
                .map(SourceInterSimpleDto::new)
                .map(SourceInterSimpleDto::getUrlId)
                .findFirst()
                .orElse(""));

        if (source.getType().equals(Source.SourceType.MANUSCRIPT)) {
            ManuscriptSource sourceMan = (ManuscriptSource) source;
            this.setDimensionSetSize(sourceMan.getDimensionsSet().size());

            this.setDimensionDtoList(sourceMan.getSortedDimensions().stream()
                    .map(DimensionsDto::new)
                    .collect(Collectors.toList()));

            this.setHandNoteDtoSet(sourceMan.getHandNoteSet().stream()
                    .map(HandNoteDto::new)
                    .collect(Collectors.toList()));

            this.setTypeNoteSet(sourceMan.getTypeNoteSet().stream()
                    .map(TypeNoteDto::new)
                    .collect(Collectors.toList()));

            setHandNoteDto(sourceMan.getHandNoteSet().stream().map(HandNoteDto::new).findFirst());
            setTypeNoteDto(sourceMan.getTypeNoteSet().stream().map(TypeNoteDto::new).findFirst());
            this.setHadLdoDLabel(sourceMan.getHasLdoDLabel());

            if (sourceMan.getFacsimile() != null) {
                if (sourceMan.getFacsimile().getSurfaces() != null) {
                    this.setSurfaceDto(sourceMan.getFacsimile().getSurfaces().stream()
                            .map(SurfaceDto::new)
                            .collect(Collectors.toList()));
                    setSurfaceString(sourceMan.getFacsimile().getSurfaces().stream().map(Surface_Base::getGraphic).collect(Collectors.toList()));

                }
            }


        } else {
            PrintedSource sourcePri = (PrintedSource) source;
            if (sourcePri.getFacsimile() != null) {
                if (sourcePri.getFacsimile().getSurfaces() != null) {
                    this.setSurfaceDto(sourcePri.getFacsimile().getSurfaces().stream()
                            .map(SurfaceDto::new)
                            .collect(Collectors.toList()));
                    setSurfaceString(sourcePri.getFacsimile().getSurfaces().stream().map(Surface_Base::getGraphic).collect(Collectors.toList()));

                }
            }
        }
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Integer getDimensionSetSize() {
        return dimensionSetSize;
    }

    public void setDimensionSetSize(Integer dimensionSetSize) {
        this.dimensionSetSize = dimensionSetSize;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Boolean getHadLdoDLabel() {
        return hadLdoDLabel;
    }

    public void setHadLdoDLabel(Boolean hadLdoDLabel) {
        this.hadLdoDLabel = hadLdoDLabel;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public String getPubPlace() {
        return pubPlace;
    }

    public void setPubPlace(String pubPlace) {
        this.pubPlace = pubPlace;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAltIdentifier() {
        return altIdentifier;
    }

    public void setAltIdentifier(String altIdentifier) {
        this.altIdentifier = altIdentifier;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<DimensionsDto> getDimensionDtoList() {
        return dimensionDtoList;
    }

    public void setDimensionDtoList(List<DimensionsDto> dimensionDtoList) {
        this.dimensionDtoList = dimensionDtoList;
    }

    public List<HandNoteDto> getHandNoteDtoSet() {
        return handNoteDtoSet;
    }

    public void setHandNoteDtoSet(List<HandNoteDto> handNoteDtoSet) {
        this.handNoteDtoSet = handNoteDtoSet;
    }

    public List<TypeNoteDto> getTypeNoteSet() {
        return typeNoteSet;
    }

    public void setTypeNoteSet(List<TypeNoteDto> typeNoteSet) {
        this.typeNoteSet = typeNoteSet;
    }

    public List<SurfaceDto> getSurfaceDto() {
        return surfaceDto;
    }

    public void setSurfaceDto(List<SurfaceDto> surfaceDto) {
        this.surfaceDto = surfaceDto;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<SourceInterSimpleDto> getSourceInterSet() {
        return sourceInterSet;
    }

    public void setSourceInterSet(List<SourceInterSimpleDto> sourceInterSet) {
        this.sourceInterSet = sourceInterSet;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Optional<HandNoteDto> getHandNoteDto() {
        return handNoteDto;
    }

    public void setHandNoteDto(Optional<HandNoteDto> handNoteDto) {
        this.handNoteDto = handNoteDto;
    }

    public Optional<TypeNoteDto> getTypeNoteDto() {
        return typeNoteDto;
    }

    public void setTypeNoteDto(Optional<TypeNoteDto> typeNoteDto) {
        this.typeNoteDto = typeNoteDto;
    }

    public List<String> getSurfaceString() {
        return surfaceString;
    }

    public void setSurfaceString(List<String> surfaceString) {
        this.surfaceString = surfaceString;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getHeteronym() {
        return heteronym;
    }

    public void setHeteronym(String heteronym) {
        this.heteronym = heteronym;
    }

    public boolean isHeteronymNull() {
        return heteronymNull;
    }

    public void setHeteronymNull(boolean heteronymNull) {
        this.heteronymNull = heteronymNull;
    }
}