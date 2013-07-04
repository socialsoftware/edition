package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.domain.ManuscriptSource.Medium;

public class HandNote extends HandNote_Base {

	public HandNote(Medium medium, String note) {
		setMedium(medium);
		setNote(note);
	}

}
