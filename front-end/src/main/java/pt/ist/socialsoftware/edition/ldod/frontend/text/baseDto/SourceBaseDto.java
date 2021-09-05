//package pt.ist.socialsoftware.edition.ldod.frontend.text.baseDto;
//
//
//import pt.ist.fenixframework.Atomic;
//import pt.ist.socialsoftware.edition.ldod.frontend.text.textDto.*;
//import pt.ist.socialsoftware.edition.ldod.frontend.utils.enums.Form;
//import pt.ist.socialsoftware.edition.ldod.frontend.utils.enums.Material;
//import pt.ist.socialsoftware.edition.ldod.frontend.utils.enums.Source_Type;
//
//import java.util.List;
//import java.util.Set;
//
//public class SourceBaseDto {
//
//    private String xmlId;
//
//    //cached attributes
//    private String name;
//    private Source_Type type;
//    private String title;
//    private String idno;
//    private String altIdentifier;
//    private String journal;
//    private String issue;
//    private String pubPlace;
//    private String notes;
//    private int startPage;
//    private int endPage;
//    private Material material;
//    private Form form;
//    private int columns;
//    private boolean ldoDLabel;
//    private boolean hasHandNoteSet;
//    private boolean hasTypeNoteSet;
//
//    private LdoDDateDto ldoDDate;
//    private Set<ManuscriptNote> handNoteSet;
//    private Set<ManuscriptNote> typeNoteSet;
//    private List<SurfaceDto> surfaces;
//    private List<DimensionsDto> sortedDimensionsDto;
//    private Set<ScholarInterDto> sourceInters;
//
//
//   public SourceBaseDto() {}
//
//    public String getXmlId() {
//        return this.xmlId;
//    }
//
//    public void setXmlId(String xmlId) {
//        this.xmlId = xmlId;
//    }
//
//    public String getName(){
//        return this.name;
//    }
//
//    public String getTitle() {
//        /*return getSourceByXmlId(this.xmlId)
//                .filter(PrintedSource.class::isInstance)
//                .map(PrintedSource.class::cast)
//                .map(printedSource -> printedSource.getTitle())
//                .orElseThrow(LdoDException::new);*/
//        return this.title;
//    }
//
//    public String getIdno() {
//        /*return getSourceByXmlId(this.xmlId)
//                .filter(ManuscriptSource.class::isInstance)
//                .map(ManuscriptSource.class::cast)
//                .map(ManuscriptSource::getIdno)
//                .orElse(null);*/
//        return this.idno;
//    }
//
//    public Material getMaterial() {
//        /*return getSourceByXmlId(this.xmlId)
//                .filter(ManuscriptSource.class::isInstance)
//                .map(ManuscriptSource.class::cast)
//                .map(ManuscriptSource::getMaterial)
//                .orElseThrow(LdoDException::new);*/
//        return this.material;
//    }
//
//
//    public int getColumns() {
//       /* return getSourceByXmlId(this.xmlId)
//                .filter(ManuscriptSource.class::isInstance)
//                .map(ManuscriptSource.class::cast)
//                .map(ManuscriptSource::getColumns)
//                .orElseThrow(LdoDException::new);*/
//       return this.columns;
//    }
//
//    public String getJournal() {
//       /* return getSourceByXmlId(this.xmlId)
//                .filter(PrintedSource.class::isInstance)
//                .map(PrintedSource.class::cast)
//                .map(PrintedSource::getJournal)
//                .orElse(null);*/
//       return this.journal;
//    }
//
//    public String getIssue() {
//        /*return getSourceByXmlId(this.xmlId)
//                .filter(PrintedSource.class::isInstance)
//                .map(PrintedSource.class::cast)
//                .map(PrintedSource::getIssue)
//                .orElse(null);*/
//        return this.issue;
//    }
//
//    public String getAltIdentifier() {
//       /* return getSourceByXmlId(this.xmlId)
//                .map(Source::getAltIdentifier)
//                .orElse(null);*/
//       return this.altIdentifier;
//    }
//
//    public String getNotes() {
//        /*return getSourceByXmlId(this.xmlId)
//                .filter(PrintedSource.class::isInstance)
//                .map(ManuscriptSource.class::cast)
//                .map(ManuscriptSource::getNotes)
//                .orElse(null);*/
//        return this.notes;
//    }
//
//    public int getStartPage() {
//        /*return getSourceByXmlId(this.xmlId)
//                .filter(PrintedSource.class::isInstance)
//                .map(PrintedSource.class::cast)
//                .map(PrintedSource::getStartPage)
//                .orElseThrow(LdoDException::new);*/
//        return this.startPage;
//    }
//
//    public int getEndPage() {
//        /*return getSourceByXmlId(this.xmlId)
//                .filter(PrintedSource.class::isInstance)
//                .map(PrintedSource.class::cast)
//                .map(PrintedSource::getEndPage)
//                .orElseThrow(LdoDException::new);*/
//        return this.endPage;
//    }
//
//    public String getPubPlace() {
//       /* return getSourceByXmlId(this.xmlId)
//                .filter(PrintedSource.class::isInstance)
//                .map(PrintedSource.class::cast)
//                .map(PrintedSource::getPubPlace)
//                .orElse(null);*/
//       return this.pubPlace;
//    }
//
//    public boolean getHasLdoDLabel() {
//        /*return getSourceByXmlId(this.xmlId)
//                .filter(ManuscriptSource.class::isInstance)
//                .map(ManuscriptSource.class::cast)
//                .map(ManuscriptSource::getHasLdoDLabel)
//                .orElseThrow(LdoDException::new);*/
//        return this.ldoDLabel;
//    }
//
//    public boolean hasHandNoteSet() {
//       /* return getSourceByXmlId(this.xmlId)
//                .filter(ManuscriptSource.class::isInstance)
//                .map(ManuscriptSource.class::cast)
//                .map(manuscriptSource -> !manuscriptSource.getHandNoteSet().isEmpty())
//                .orElseThrow(LdoDException::new);*/
//       return this.hasHandNoteSet;
//    }
//
//    public boolean hasTypeNoteSet() {
//        /*return getSourceByXmlId(this.xmlId)
//                .filter(ManuscriptSource.class::isInstance)
//                .map(ManuscriptSource.class::cast)
//                .map(manuscriptSource -> !manuscriptSource.getTypeNoteSet().isEmpty())
//                .orElseThrow(LdoDException::new);*/
//        return this.hasTypeNoteSet;
//    }
//
//    public Source_Type getType() {
//        //return getSourceByXmlId(this.xmlId).map(source -> source.getType()).orElse(null);
//        return this.type;
//    }
//
//
//
//    public Form getForm() {
//       /* return getSourceByXmlId(this.xmlId)
//                .filter(ManuscriptSource.class::isInstance)
//                .map(ManuscriptSource.class::cast)
//                .map(ManuscriptSource::getForm)
//                .orElse(null);*/
//       return this.form;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setType(Source_Type type) {
//        this.type = type;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public void setIdno(String idno) {
//        this.idno = idno;
//    }
//
//    public void setAltIdentifier(String altIdentifier) {
//        this.altIdentifier = altIdentifier;
//    }
//
//    public void setJournal(String journal) {
//        this.journal = journal;
//    }
//
//    public void setIssue(String issue) {
//        this.issue = issue;
//    }
//
//    public void setPubPlace(String pubPlace) {
//        this.pubPlace = pubPlace;
//    }
//
//    public void setNotes(String notes) {
//        this.notes = notes;
//    }
//
//    public void setStartPage(int startPage) {
//        this.startPage = startPage;
//    }
//
//    public void setEndPage(int endPage) {
//        this.endPage = endPage;
//    }
//
//    public void setMaterial(Material material) {
//        this.material = material;
//    }
//
//    public void setForm(Form form) {
//        this.form = form;
//    }
//
//    public void setColumns(int columns) {
//        this.columns = columns;
//    }
//
//    public boolean isLdoDLabel() {
//        return ldoDLabel;
//    }
//
//    public void setLdoDLabel(boolean ldoDLabel) {
//        this.ldoDLabel = ldoDLabel;
//    }
//
//    public boolean isHasHandNoteSet() {
//        return hasHandNoteSet;
//    }
//
//    public void setHasHandNoteSet(boolean hasHandNoteSet) {
//        this.hasHandNoteSet = hasHandNoteSet;
//    }
//
//    public boolean isHasTypeNoteSet() {
//        return hasTypeNoteSet;
//    }
//
//    public void setHasTypeNoteSet(boolean hasTypeNoteSet) {
//        this.hasTypeNoteSet = hasTypeNoteSet;
//    }
//
//    public LdoDDateDto getLdoDDate() {
//        return ldoDDate;
//    }
//
//    public void setLdoDDate(LdoDDateDto ldoDDate) {
//        this.ldoDDate = ldoDDate;
//    }
//
//    public Set<ManuscriptNote> getHandNoteSet() {
//        return handNoteSet;
//    }
//
//    public void setHandNoteSet(Set<ManuscriptNote> handNoteSet) {
//        this.handNoteSet = handNoteSet;
//    }
//
//    public Set<ManuscriptNote> getTypeNoteSet() {
//        return typeNoteSet;
//    }
//
//    public void setTypeNoteSet(Set<ManuscriptNote> typeNoteSet) {
//        this.typeNoteSet = typeNoteSet;
//    }
//
//    public List<SurfaceDto> getSurfaces() {
//        return surfaces;
//    }
//
//    public void setSurfaces(List<SurfaceDto> surfaces) {
//        this.surfaces = surfaces;
//    }
//
//    public List<DimensionsDto> getSortedDimensionsDto() {
//        return sortedDimensionsDto;
//    }
//
//    public void setSortedDimensionsDto(List<DimensionsDto> sortedDimensions) {
//        this.sortedDimensionsDto = sortedDimensions;
//    }
//
//    public Set<ScholarInterDto> getSourceIntersSet() {
//        return sourceInters;
//    }
//
//    public void setSourceIntersSet(Set<ScholarInterDto> sourceInters) {
//        this.sourceInters = sourceInters;
//    }
//}