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
	public int getLength(FragInter inter) {
		int length = 0;

		for (TextPortion child : getChildTextSet()) {
			return child.getLength(inter);
		}

		return length;
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
	}

}
