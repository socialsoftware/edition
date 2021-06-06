package pt.ist.socialsoftware.edition.ldod.frontend.text.baseDto;


import pt.ist.socialsoftware.edition.ldod.frontend.utils.enums.Form;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.enums.Material;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.enums.Source_Type;

public class SourceBaseDto {

    private String xmlId;

    //cached attributes
    private String name;
    private Source_Type type;
    private String title;
    private String idno;
    private String altIdentifier;
    private String journal;
    private String issue;
    private String pubPlace;
    private String notes;
    private int startPage;
    private int endPage;
    private Material material;
    private Form form;
    private int columns;
    private boolean ldoDLabel;
    private boolean hasHandNoteSet;
    private boolean hasTypeNoteSet;

   public SourceBaseDto() {}

    public String getXmlId() {
        return this.xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getName(){
        return this.name;
    }

    public String getTitle() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(printedSource -> printedSource.getTitle())
                .orElseThrow(LdoDException::new);*/
        return this.title;
    }

    public String getIdno() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getIdno)
                .orElse(null);*/
        return this.idno;
    }

    public Material getMaterial() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getMaterial)
                .orElseThrow(LdoDException::new);*/
        return this.material;
    }


    public int getColumns() {
       /* return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getColumns)
                .orElseThrow(LdoDException::new);*/
       return this.columns;
    }

    public String getJournal() {
       /* return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getJournal)
                .orElse(null);*/
       return this.journal;
    }

    public String getIssue() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getIssue)
                .orElse(null);*/
        return this.issue;
    }

    public String getAltIdentifier() {
       /* return getSourceByXmlId(this.xmlId)
                .map(Source::getAltIdentifier)
                .orElse(null);*/
       return this.altIdentifier;
    }

    public String getNotes() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getNotes)
                .orElse(null);*/
        return this.notes;
    }

    public int getStartPage() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getStartPage)
                .orElseThrow(LdoDException::new);*/
        return this.startPage;
    }

    public int getEndPage() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getEndPage)
                .orElseThrow(LdoDException::new);*/
        return this.endPage;
    }

    public String getPubPlace() {
       /* return getSourceByXmlId(this.xmlId)
                .filter(PrintedSource.class::isInstance)
                .map(PrintedSource.class::cast)
                .map(PrintedSource::getPubPlace)
                .orElse(null);*/
       return this.pubPlace;
    }

    public boolean getHasLdoDLabel() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getHasLdoDLabel)
                .orElseThrow(LdoDException::new);*/
        return this.ldoDLabel;
    }

    public boolean hasHandNoteSet() {
       /* return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(manuscriptSource -> !manuscriptSource.getHandNoteSet().isEmpty())
                .orElseThrow(LdoDException::new);*/
       return this.hasHandNoteSet;
    }

    public boolean hasTypeNoteSet() {
        /*return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(manuscriptSource -> !manuscriptSource.getTypeNoteSet().isEmpty())
                .orElseThrow(LdoDException::new);*/
        return this.hasTypeNoteSet;
    }

    public Source_Type getType() {
        //return getSourceByXmlId(this.xmlId).map(source -> source.getType()).orElse(null);
        return this.type;
    }



    public Form getForm() {
       /* return getSourceByXmlId(this.xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getForm)
                .orElse(null);*/
       return this.form;
    }
}