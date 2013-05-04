package pt.ist.socialsoftware.edition.domain;

public abstract class LdoDText extends LdoDText_Base implements GraphElement {

	public enum OpenClose {
		OPEN, CLOSE, NO;
	};

	public LdoDText() {
		super();
		setOpenClose(OpenClose.NO);
		setNextText(null);
	}

	public void remove() {
		removeReadingOfFirst();

		removeReading();

		removeNextText();

		deleteDomainObject();
	}

	public abstract String writeHtml();

	public String writeReference(int refsCounter) {
		return null;
	}

	public String writeNote(int refsCounter) {
		return null;
	}
}
