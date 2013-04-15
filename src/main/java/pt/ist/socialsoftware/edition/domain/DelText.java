package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class DelText extends DelText_Base {

	public enum HowDel {
		OVERSTRIKE, OVERTYPED, OVERWRITTEN, UNSPECIFIED;
	};

	public DelText(OpenClose value, HowDel how) {
		super();
		setOpenClose(value);
		setHow(how);
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

}
