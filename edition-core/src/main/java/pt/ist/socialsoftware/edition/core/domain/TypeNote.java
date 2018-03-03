package pt.ist.socialsoftware.edition.core.domain;

import pt.ist.socialsoftware.edition.core.domain.TypeNote_Base;
import pt.ist.socialsoftware.edition.core.domain.ManuscriptSource.Medium;

public class TypeNote extends TypeNote_Base {

	public TypeNote(Medium medium, String note) {
		setMedium(medium);
		setNote(note);
	}

	@Override
	public void remove() {
		setManuscript(null);

		super.remove();
	}

}
