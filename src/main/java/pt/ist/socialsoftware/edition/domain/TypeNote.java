package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.domain.ManuscriptSource.Medium;

public class TypeNote extends TypeNote_Base {

	public TypeNote(Medium medium, String note) {
		setMedium(medium);
		setNote(note);
	}

}
