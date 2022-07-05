package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.domain.DelText_Base;
import pt.ist.socialsoftware.edition.ldod.generators.TextPortionVisitor;

public class DelText extends DelText_Base {

	public enum HowDel {
		OVERSTRIKE("overstrike"), OVERTYPED("overtyped"), OVERWRITTEN("overwritten"), UNSPECIFIED("unspecified");

		private String desc;

		HowDel(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return this.desc;
		}
	};

	public DelText(TextPortion parent, HowDel how) {
		parent.addChildText(this);
		setHow(how);
	}

	@Override
	public void accept(TextPortionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Boolean isFormat(Boolean displayDel, Boolean highlightSubst, FragInter fragInter) {
		if (getInterps().contains(fragInter) && displayDel) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getNote() {
		String result = "Retirado - " + getHow().toString();

		return result;
	}

}
