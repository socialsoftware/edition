package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource_Base;

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
		getDimensionsSet().stream().forEach(d -> d.remove());

		for (HandNote handNote : getHandNoteSet()) {
			handNote.remove();
		}

		for (TypeNote typeNote : getTypeNoteSet()) {
			typeNote.remove();
		}

		super.remove();
	}

	public List<Dimensions> getSortedDimensions() {
		return getDimensionsSet().stream().sorted((d1, d2) -> d1.getPosition() - d2.getPosition())
				.collect(Collectors.toList());
	}

}
