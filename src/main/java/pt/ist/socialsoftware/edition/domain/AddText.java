package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class AddText extends AddText_Base {

	public enum Place {
		ABOVE, SUPERIMPOSED, MARGIN, TOP, INLINE, UNSPECIFIED;
	};

	public AddText() {
		super();
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

}
