package pt.ist.socialsoftware.edition.core.domain;

import pt.ist.socialsoftware.edition.core.domain.PhysNote_Base;

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
