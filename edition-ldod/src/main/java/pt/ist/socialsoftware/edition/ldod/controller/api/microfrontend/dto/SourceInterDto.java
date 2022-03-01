package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import lombok.Getter;
import lombok.Setter;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource.Form;
import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource.Material;
import pt.ist.socialsoftware.edition.ldod.domain.Source.SourceType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Getter
@Setter
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


    public SourceInterDto(SourceInter sourceInter) {

        this.sourceType = sourceInter.getSource().getType();
        this.setShortName(sourceInter.getShortName());
        this.setExternalId(sourceInter.getExternalId());
        setHeteronym(sourceInter.getHeteronym().getName());
        if (sourceInter.getLdoDDate() != null) {
            this.setDate(sourceInter.getLdoDDate().print());
            if (sourceInter.getLdoDDate().getPrecision() != null) {
                this.setDesc(sourceInter.getLdoDDate().getPrecision().getDesc());
            }
        }


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
            setXmlId(source.getSourceIntersSet().stream()
                    .map(SourceInterSimpleDto::new).findFirst().get().getXmlId());
            setUrlId(source.getSourceIntersSet().stream()
                    .map(SourceInterSimpleDto::new).findFirst().get().getUrlId());

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
            this.title = source.getTitle();
            this.setJournal(source.getJournal());
            this.setIssue(source.getIssue());
            this.setStartPage(source.getStartPage());
            this.setEndPage(source.getEndPage());
            this.setPubPlace(source.getPubPlace());
            this.setAltIdentifier(source.getAltIdentifier());


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
        this.setSourceInterSet(source.getSourceIntersSet().stream()
                .map(SourceInterSimpleDto::new)
                .collect(Collectors.toList()));
        setHeteronym(source.getHeteronym().getName());
        setTranscription(source.getFragment().getRepresentativeSourceInter().getTitle());
        setXmlId(source.getSourceIntersSet().stream()
                .map(SourceInterSimpleDto::new).findFirst().get().getXmlId());
        setUrlId(source.getSourceIntersSet().stream()
                .map(SourceInterSimpleDto::new).findFirst().get().getUrlId());

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
}