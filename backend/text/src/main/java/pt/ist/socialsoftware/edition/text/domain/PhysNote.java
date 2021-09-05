package pt.ist.socialsoftware.edition.text.domain;

public class PhysNote extends PhysNote_Base {

	public PhysNote() {
		super();
	}

	public void remove() {
		for (TextPortion text : getTextPortionSet()) {
			removeTextPortion(text);
		}

		deleteDomainObject();
	}

}
