package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class RdgGrpText extends RdgGrpText_Base {

	public RdgGrpText(TextPortion parent, VariationType type) {
		parent.addChildText(this);
		setType(type);
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
	}

}
