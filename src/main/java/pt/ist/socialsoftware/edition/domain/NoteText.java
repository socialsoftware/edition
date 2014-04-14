package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class NoteText extends NoteText_Base {

	public enum NoteType {
		ANNEX("annex");

		private final String desc;

		NoteType(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public NoteText(TextPortion parent, NoteType type) {
		parent.addChildText(this);

		setType(type);

		if (type == NoteType.ANNEX) {
			for (FragInter inter : getInterps()) {
				new AnnexNote(inter, this);
			}
		}
	}

	@Override
	public void remove() {
		for (AnnexNote annexNote : getAnnexNoteSet()) {
			annexNote.remove();
		}

		super.remove();
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
	}

	// TODO: To consider whether a complete generation, without using the
	// visitors will be needed in the future
	public String generatePresentationText() {
		String result = "";

		TextPortion text = getFirstChildText();
		while (text != null) {
			if (text instanceof SimpleText) {
				SimpleText stext = (SimpleText) text;
				result = result + stext.getValue();
			}

			text = text.getNextText();

		}

		return result;
	}

}
