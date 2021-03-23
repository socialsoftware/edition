package pt.ist.socialsoftware.edition.text.api.dto;

import pt.ist.socialsoftware.edition.text.domain.InfoRange;

public class InfoRangeDto {

    private String externalId;
    private String scholarInterid;
    private String start;
    private String end;
    private String quote;
    private String text;
    private int startOffset;
    private int endOffset;

    public InfoRangeDto (InfoRange infoRange) {
        this.scholarInterid = infoRange.getScholarInter().getXmlId();
        this.externalId = infoRange.getExternalId();
        this.start = infoRange.getStart();
        this.end = infoRange.getEnd();
        this.quote = infoRange.getQuote();
        this.text = infoRange.getText();
        this.startOffset = infoRange.getStartOffset();
        this.endOffset = infoRange.getEndOffset();
    }

    public String getExternalId() {
        return externalId;
    }

    public String getScholarInterid() {
        return scholarInterid;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getText() {
        return text;
    }

    public String getQuote() {
        return quote;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }
}
