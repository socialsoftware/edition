package pt.ist.socialsoftware.edition.domain;


public abstract class LdoDText extends LdoDText_Base implements GraphElement {

	public LdoDText() {
		super();
	}

	public void printAll() {
		print();
		if (getNextText() != null) {
			getNextText().printAll();
		}
	}

	public abstract void print();

}
