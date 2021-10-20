package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.InfoRange;

public class InfoRangeDto {
	private String xmlId;
	private String text;
	private String quote;
	private String start;
	private String end;
	private int startOffset;
	private int endOffset;

	public InfoRangeDto(InfoRange infoRange) {
		this.setXmlId(infoRange.getFragInter().getXmlId());
		this.setText(infoRange.getText());
		this.setQuote(infoRange.getQuote());
		this.setStart(infoRange.getStart());
		this.setEnd(infoRange.getEnd());
		this.setStartOffset(infoRange.getStartOffset());
		this.setEndOffset(infoRange.getEndOffset());
	}

	public String getXmlId() {
		return xmlId;
	}

	public void setXmlId(String xmlId) {
		this.xmlId = xmlId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}

	public int getEndOffset() {
		return endOffset;
	}

	public void setEndOffset(int endOffset) {
		this.endOffset = endOffset;
	}

}
