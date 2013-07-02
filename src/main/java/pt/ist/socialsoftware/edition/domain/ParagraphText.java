package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class ParagraphText extends ParagraphText_Base {

	public ParagraphText(TextPortion parent) {
		parent.addChildText(this);
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
	}

}
