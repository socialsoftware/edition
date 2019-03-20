package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.domain.AnnexNote_Base;

public class AnnexNote extends AnnexNote_Base implements Comparable<AnnexNote> {

	public AnnexNote(ScholarInter inter, NoteText noteText) {
		setScholarInter(inter);
		setNoteText(noteText);

		int number = getScholarInter().getNumAnnexNotes() + 1;
		setNumber(number);
		getScholarInter().setNumAnnexNotes(number);
	}

	public AnnexNote(ScholarInter inter) {
		setScholarInter(inter);

		int number = getScholarInter().getNumAnnexNotes() + 1;
		setNumber(number);
		getScholarInter().setNumAnnexNotes(number);
	}

	public void remove() {
		setScholarInter(null);
		setNoteText(null);

		deleteDomainObject();
	}

	@Override
	public int compareTo(AnnexNote o) {
		return getNumber() > o.getNumber() ? +1
				: getNumber() < o.getNumber() ? -1 : 0;
	}

}
