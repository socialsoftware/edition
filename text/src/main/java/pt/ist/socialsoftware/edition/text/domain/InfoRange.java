package pt.ist.socialsoftware.edition.text.domain;

public class InfoRange extends InfoRange_Base {


	public InfoRange(Citation citation, ScholarInter scholarInter, String start, int startOffset, String end, int endOffset,
                     String quote, String text, long citationId) {
		setCitation(citation);
		setScholarInter(scholarInter);

		setStart(start);
		setStartOffset(startOffset);
		setEnd(end);
		setEndOffset(endOffset);

		setQuote(quote);
		setText(text);

		setId(citationId);
	}


	// adicionado recentemente, testar
	public void remove() {
		setScholarInter(null);
		setCitation(null);
		deleteDomainObject();
	}
}
