package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.domain.RefText.RefType;
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

	public NoteText(AnnexNote annexNote, NoteType type) {
		setParentText(null);
		setType(type);
		addAnnexNote(annexNote);
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
				result = result + stext.getValue() + " ";
			} else if (text instanceof RefText) {
				RefText refText = (RefText) text;
				String link = "#";
				if (refText.getType() == RefType.GRAPHIC) {
					link = "/facs/" + refText.getSurface().getGraphic();
				} else if (refText.getType() == RefType.WITNESS) {
					link = "/fragments/fragment/inter/"
							+ refText.getFragInter().getExternalId();
				}
				result = result + "<a href=\"" + link + "\">";
				SimpleText childText = (SimpleText) text.getFirstChildText();
				result = result + childText.getValue() + "</a> ";
			}

			text = text.getNextText();
		}

		return result;
	}
}
