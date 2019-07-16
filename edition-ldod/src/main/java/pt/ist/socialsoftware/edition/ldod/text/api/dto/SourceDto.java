package pt.ist.socialsoftware.edition.ldod.text.api.dto;

import org.joda.time.LocalDate;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;

import java.util.*;
import java.util.stream.Collectors;

public class SourceDto {
    private String xmlId;

    public SourceDto(Source source) {
        setXmlId(source.getXmlId());
    }

    public String getXmlId() {
        return this.xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getTitle() {
        return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(printedSource -> printedSource.getTitle())
                .orElseThrow(LdoDException::new);
    }

    public String getIdno(){
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getIdno)
                .orElse(null);
    }

    public ManuscriptSource.Material getMaterial(){
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getMaterial)
                .orElseThrow(LdoDException::new);
    }

    public String getFormattedDimensions(){
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getDimensionsSet)
                .orElse(new HashSet<>())
                .stream().map(dimensions -> dimensions.getHeight() + "x" + dimensions.getWidth())
                .collect(Collectors.joining(";"));
    }

    public List<AbstractMap.SimpleEntry<String, String>> getFormattedHandNote(){
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getHandNoteSet)
                .orElse(new HashSet<>())
                .stream().map(handNote ->
                        new AbstractMap.SimpleEntry<>((handNote.getMedium() != null ? handNote.getMedium().getDesc() : ""), handNote.getNote()))
                .collect(Collectors.toList());
    }

    public List<AbstractMap.SimpleEntry<String, String>> getFormattedTypeNote(){
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getTypeNoteSet)
                .orElse(new HashSet<>())
                .stream().map(typeNote ->
                        new AbstractMap.SimpleEntry<>((typeNote.getMedium() != null ? typeNote.getMedium().getDesc() : ""), typeNote.getNote()))
                .collect(Collectors.toList());
    }

    public int getColumns(){
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getColumns)
                .orElseThrow(LdoDException::new);
    }

    public String getJournal(){
        return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getJournal)
                .orElse(null);
    }

    public String getIssue(){
        return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getIssue)
                .orElse(null);
    }

    public String getAltIdentifier(){
        return getSourceByXmlId(this.xmlId)
                .map(Source::getAltIdentifier)
                .orElse(null);
    }

    public String getNotes(){
        return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getNotes)
                .orElse(null);
    }

    public int getStartPage(){
        return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getStartPage)
                .orElseThrow(LdoDException::new);
    }

    public int getEndPage(){
        return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getEndPage)
                .orElseThrow(LdoDException::new);
    }

    public String getPubPlace(){
        return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getPubPlace)
                .orElse(null);
    }

    public boolean hasLdoDLabel() {
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getHasLdoDLabel)
                .orElseThrow(LdoDException::new);
    }

    public boolean hasHandNoteSet() {
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(manuscriptSource -> !manuscriptSource.getHandNoteSet().isEmpty())
                .orElseThrow(LdoDException::new);
    }

    public boolean hasTypeNoteSet() {
        return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(manuscriptSource -> !manuscriptSource.getTypeNoteSet().isEmpty())
                .orElseThrow(LdoDException::new);
    }

    public LocalDate getLdoDDate() {
        return getSourceByXmlId(this.xmlId)
                .map(source -> source.getLdoDDate() != null ? source.getLdoDDate().getDate() : null)
                .orElse(null);
    }

    public LdoDDateDto getLdoDDateDto() {
        return getSourceByXmlId(this.xmlId)
                .map(Source_Base::getLdoDDate)
                .map(LdoDDateDto::new)
                .orElse(null);
    }

    public List<SurfaceDto> getSurfaces(){
      List<Surface> surfaces =  getSourceByXmlId(this.xmlId)
                .map(Source_Base::getFacsimile)
                .map(Facsimile::getSurfaces).orElse(new ArrayList<>());
      return surfaces.stream().map(SurfaceDto::new).collect(Collectors.toList());
    }

    public Source.SourceType getType() {
        return getSourceByXmlId(this.xmlId).map(source -> source.getType()).orElse(null);
    }

    private Optional<Source> getSourceByXmlId(String xmlId) {
        return TextModule.getInstance().getFragmentsSet().stream()
                .flatMap(fragment -> fragment.getSourcesSet().stream())
                .filter(source -> source.getXmlId().equals(xmlId))
                .findAny();
    }

}