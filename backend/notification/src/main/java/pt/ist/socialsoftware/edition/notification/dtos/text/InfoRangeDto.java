package pt.ist.socialsoftware.edition.notification.dtos.text;


public class InfoRangeDto {

    private String externalId;
    private String scholarInterid;
    private String start;
    private String end;
    private String quote;
    private String text;
    private int startOffset;
    private int endOffset;



    public InfoRangeDto() {
        super();
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

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setScholarInterid(String scholarInterid) {
        this.scholarInterid = scholarInterid;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }
}
