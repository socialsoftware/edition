package pt.ist.socialsoftware.edition.text.domain;


import pt.ist.socialsoftware.edition.text.domain.ManuscriptSource.Medium;

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
