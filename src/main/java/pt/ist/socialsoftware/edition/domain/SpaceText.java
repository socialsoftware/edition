package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class SpaceText extends SpaceText_Base {

	public enum SpaceDim {
		VERTICAL("vertical"), HORIZONTAL("horizontal"), UNKNOWN("unknown");

		private String desc;

		SpaceDim(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public enum SpaceUnit {
		MINIMS("minims"), UNKNOWN("unknown");

		private String desc;

		SpaceUnit(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public SpaceText(TextPortion parent, SpaceDim dim, int quantity,
			SpaceUnit unit) {
		parent.addChildText(this);
		setDim(dim);
		setQuantity(quantity);
		setUnit(unit);
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
	}

}
