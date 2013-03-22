package pt.ist.socialsoftware.edition.domain;

public abstract class LdoDText extends LdoDText_Base implements GraphElement {

	public enum OpenClose {
		OPEN, CLOSE;
	};

	public LdoDText() {
		super();
	}

	public void remove() {
		removeReading();

		removeNextText();

		deleteDomainObject();

	}

}
