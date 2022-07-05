package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.domain.HandNote_Base;

public class HandNote extends HandNote_Base {

	public HandNote(ManuscriptSource.Medium medium, String note) {
		setMedium(medium);
		setNote(note);
	}

	@Override
	public void remove() {
		setManuscript(null);

		super.remove();
	}

}
