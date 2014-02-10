package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class AppText extends AppText_Base {

	public AppText(TextPortion parent) {
		if (parent != null) {
			parent.addChildText(this);
		}
	}

	public AppText() {
		setParentText(null);
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
	}

}
