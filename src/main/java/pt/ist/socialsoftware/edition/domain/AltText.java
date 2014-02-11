package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class AltText extends AltText_Base {

	public enum AltMode {
		EXCL("excl"), INCL("incl"), NONSPECIFIED("nonspecified");

		private final String desc;

		AltMode(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public AltText(TextPortion parent, SegText segTextOne, SegText segTextTwo,
			AltMode mode, double weightOne, double weightTwo) {
		parent.addChildText(this);

		setSegTextOne(segTextOne);
		setSegTextTwo(segTextTwo);
		setMode(mode);
		setWeightOne(weightOne);
		setWeightTwo(weightTwo);
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void remove() {
		setSegTextOne(null);
		setSegTextTwo(null);

		super.remove();
	}

}
