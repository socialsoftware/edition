package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.domain.GapText_Base;
import pt.ist.socialsoftware.edition.ldod.generators.TextPortionVisitor;

public class GapText extends GapText_Base {

	public enum GapReason {
		IRRELEVANT("irrelevant"), ILLEGIABLE("illegible"), NONSPECIFIED(
				"nonspecified");

		private String desc;

		GapReason(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public enum GapUnit {
		WORD("word"), CHAR("char"), NONSPECIFIED("nonspecified");

		private String desc;

		GapUnit(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public GapText(TextPortion parent, GapReason reason, int extent,
			GapUnit unit) {
		parent.addChildText(this);

		setReason(reason);
		setExtent(extent);
		setUnit(unit);
	}

	@Override
	public void accept(TextPortionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String getSeparator(FragInter inter) {
		if (getInterps().contains(inter)) {
			String separator = null;

			if (!super.getBreakWord()) {
				separator = "";
			} else {
				separator = " ";
			}
			return separator;
		} else {
			return super.getSeparator(inter);
		}
	}

	public String getGapValue() {
		String gapValue = "<span style=\"color: rgb(0,0,255);\">[ileg.]</span>";
		// String gapValue = "<";
		// String symbol = getUnit() == GapUnit.WORD ? "######" : "#";
		//
		// for (int i = 0; i < getExtent(); i++) {
		// if (getUnit() == GapUnit.WORD) {
		// gapValue = gapValue + symbol;
		// if (i + 1 != getExtent()) {
		// gapValue = gapValue + " ";
		// }
		// } else {
		// gapValue = gapValue + symbol;
		// }
		// }
		// gapValue = gapValue + ">";

		return gapValue;
	}
}
