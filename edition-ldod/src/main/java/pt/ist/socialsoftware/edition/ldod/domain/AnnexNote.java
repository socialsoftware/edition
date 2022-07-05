package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.domain.AnnexNote_Base;

public class AnnexNote extends AnnexNote_Base implements Comparable<AnnexNote> {

	public AnnexNote(FragInter inter, NoteText noteText) {
		setFragInter(inter);
		setNoteText(noteText);

		int number = getFragInter().getNumAnnexNotes() + 1;
		setNumber(number);
		getFragInter().setNumAnnexNotes(number);
	}

	public AnnexNote(FragInter inter) {
		setFragInter(inter);

		int number = getFragInter().getNumAnnexNotes() + 1;
		setNumber(number);
		getFragInter().setNumAnnexNotes(number);
	}

	public void remove() {
		setFragInter(null);
		setNoteText(null);

		deleteDomainObject();
	}

	@Override
	public int compareTo(AnnexNote o) {
		return getNumber() > o.getNumber() ? +1
				: getNumber() < o.getNumber() ? -1 : 0;
	}

}
