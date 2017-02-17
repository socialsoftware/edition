package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.domain.ParagraphText_Base;
import pt.ist.socialsoftware.edition.generators.TextPortionVisitor;

public class ParagraphText extends ParagraphText_Base {

	public ParagraphText(TextPortion parent) {
		parent.addChildText(this);
	}

	@Override
	public void accept(TextPortionVisitor visitor) {
		visitor.visit(this);
	}

}
