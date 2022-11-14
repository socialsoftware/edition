package pt.ist.socialsoftware.edition.ldod.bff.search.dto;

import pt.ist.socialsoftware.edition.ldod.domain.*;

import java.util.List;

public class SearchFragInterDto {
    private String externalId;

    private String fragTitle;
    private String xmlId;
    private String urlId;
    private String title;
    private List<Integer> options;
    private String sourceType;
    private String sources;
    private boolean hasLdoDMark;
    private String editor;
    private String heteronym;
    private String date;

    public SearchFragInterDto(FragInter fragInter, List<Integer> options) {
        Fragment fragment = fragInter.getFragment();
        setExternalId(fragInter.getExternalId());
        setXmlId(fragment.getXmlId());
        setFragTitle(fragment.getTitle());
        setUrlId(fragInter.getUrlId());
        setSourceType(fragInter.getSourceType().toString());
        setHeteronym(fragInter.getHeteronym() != null ? fragInter.getHeteronym().getName() : null);
        setTitle(fragInter.getTitle());
        if (fragInter instanceof SourceInter) {
            Source source = ((SourceInter) fragInter).getSource();
            setSources(source);
            setHasLdoDMark(source);
            setTitle(fragInter.getShortName());
            setDate(source.getLdoDDate() != null ? source.getLdoDDate().print() : null);
        }
        if (fragInter instanceof ExpertEditionInter) {
            setEditor(((ExpertEdition) fragInter.getEdition()).getEditor());
            setTitle(String.format("%s (%s)", fragInter.getTitle(), ((ExpertEdition) fragInter.getEdition()).getEditor()));
            setDate(fragInter.getLdoDDate() != null ? fragInter.getLdoDDate().print() : null);
        }
        setOptions(options);
    }

    public String getFragTitle() {
        return fragTitle;
    }

    public void setFragTitle(String fragTitle) {
        this.fragTitle = fragTitle;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getOptions() {
        return options;
    }

    public void setOptions(List<Integer> options) {
        this.options = options;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(Source source) {
        if (source.getType().equals(Source.SourceType.MANUSCRIPT)) {
            this.sources = ((ManuscriptSource) source).getTypeNoteSet().isEmpty()
                    ? "manuscript"
                    : ((ManuscriptSource) source).getHandNoteSet().isEmpty()
                    ? "typescript"
                    : "both";
        }

    }

    public boolean isHasLdoDMark() {
        return hasLdoDMark;
    }

    public void setHasLdoDMark(Source source) {
        this.hasLdoDMark = source.getType().equals(Source.SourceType.MANUSCRIPT) && ((ManuscriptSource) source).getHasLdoDLabel();
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getHeteronym() {
        return heteronym;
    }

    public void setHeteronym(String heteronym) {
        this.heteronym = heteronym;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
