package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class AddText extends AddText_Base {

	public enum Place {
		ABOVE("above"), BELOW("below"), SUPERIMPOSED("superimposed"), MARGIN(
				"margin"), TOP("top"), BOTTOM("bottom"), INLINE("inline"), INSPACE(
				"inspace"), OVERLEAF("overleaf"), OPPOSITE("opposite"), END(
				"end"), UNSPECIFIED("unspecified");

		private String desc;

		Place(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public AddText(TextPortion parent, Place place) {
		parent.addChildText(this);
		setPlace(place);
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Boolean isFormat(Boolean displayDel, Boolean highlightSubst,
			FragInter fragInter) {
		if (getInterps().contains(fragInter)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getNote() {
		String result = "Adicionado - " + getPlace().toString();

		return result;
	}

}
