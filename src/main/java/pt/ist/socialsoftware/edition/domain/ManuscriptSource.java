package pt.ist.socialsoftware.edition.domain;

public class ManuscriptSource extends ManuscriptSource_Base {

	public enum Form {
		LEAF
	};

	public enum Material {
		PAPER
	};

	public enum Medium {
		PEN("pen"), PENCIL("pencil"), BLUE_INK("blue-ink"), BLACK_INK("black-ink"), VIOLET_INK("violet-ink"), RED_INK(
				"red-ink"), GREEN_INK("green-ink");

		private final String desc;

		Medium(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public ManuscriptSource() {
		super();
		setType(SourceType.MANUSCRIPT);
		setHasLdoDLabel(false);
	}

	@Override
	public void remove() {
		if (getDimensions() != null) {
			getDimensions().remove();
		}

		for (HandNote handNote : getHandNoteSet()) {
			handNote.remove();
		}

		for (TypeNote typeNote : getTypeNoteSet()) {
			typeNote.remove();
		}

		super.remove();
	}

}
