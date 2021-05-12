package pt.ist.socialsoftware.edition.virtual.utils;


import pt.ist.socialsoftware.edition.virtual.domain.Range;

import java.io.Serializable;

public class RangeJson implements Serializable {
	private static final long serialVersionUID = 1L;

	private String start;
	private int startOffset;
	private String end;
	private int endOffset;

	public RangeJson() {
	}

	public RangeJson(Range range) {
		setStart(range.getStart());
		setStartOffset(range.getStartOffset());
		setEnd(range.getEnd());
		setEndOffset(range.getEndOffset());
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int getEndOffset() {
		return endOffset;
	}

	public void setEndOffset(int endOffset) {
		this.endOffset = endOffset;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
