package pt.ist.socialsoftware.edition.text.domain;



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
