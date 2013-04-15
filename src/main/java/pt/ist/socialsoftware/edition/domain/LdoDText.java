package pt.ist.socialsoftware.edition.domain;

public abstract class LdoDText extends LdoDText_Base implements GraphElement {

	public enum OpenClose {
		OPEN, CLOSE;
	};

	public LdoDText() {
		super();
		setNextText(null);
	}

	public void remove() {
		removeReadingOfFirst();

		removeReading();

		removeNextText();

		deleteDomainObject();
	}

}
