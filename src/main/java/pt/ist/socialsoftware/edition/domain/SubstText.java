package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class SubstText extends SubstText_Base {

	public SubstText(OpenClose value) {
		super();
		setOpenClose(value);
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

}
