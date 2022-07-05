package pt.ist.socialsoftware.edition.ldod.domain;

public class InfoRange extends InfoRange_Base {

	public InfoRange(Citation citation, FragInter fragInter, String start, int startOffset, String end, int endOffset,
			String quote, String text) {
		setCitation(citation);
		setFragInter(fragInter);

		setStart(start);
		setStartOffset(startOffset);
		setEnd(end);
		setEndOffset(endOffset);

		setQuote(quote);
		setText(text);
	}

	// adicionado recentemente, testar
	public void remove() {
		setFragInter(null);
		setCitation(null);
		deleteDomainObject();
	}
}
