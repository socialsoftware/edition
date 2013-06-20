package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class SpaceText extends SpaceText_Base {

	public enum SpaceDim {
		VERTICAL, HORIZONTAL, UNKNOWN;
	};

	public enum SpaceUnit {
		MINIMS, UNKNOWN;
	};

	public SpaceText(SpaceDim dim, int quantity, SpaceUnit unit) {
		super();
		setDim(dim);
		setQuantity(quantity);
		setUnit(unit);
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String writeHtml() {
		String result = "";
		String separator = "";
		if (getDim() == SpaceDim.VERTICAL) {
			separator = "<br>";
			// the initial line break is for a new line
			result = separator;
		} else if (getDim() == SpaceDim.HORIZONTAL) {
			separator = "&nbsp; ";
		}

		for (int i = 0; i < getQuantity(); i++) {
			result = result + separator;
		}

		return result;
	}

}
