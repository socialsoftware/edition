package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.domain.PhysNote_Base;

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
