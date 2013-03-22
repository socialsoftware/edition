package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class DelText extends DelText_Base {

	public enum HowDel {
		OVERSTRIKE, OVERTYPED, OVERWRITTEN, UNSPECIFIED;
	};

	public DelText() {
		super();
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

}
