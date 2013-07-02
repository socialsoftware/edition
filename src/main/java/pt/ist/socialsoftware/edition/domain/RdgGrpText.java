package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class RdgGrpText extends RdgText_Base {

	public RdgGrpText() {
		super();
	}

	public RdgGrpText(TextPortion parent) {
		parent.addChildText(this);
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
	}

}
