package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.domain.ParagraphText_Base;
import pt.ist.socialsoftware.edition.ldod.generators.TextPortionVisitor;

public class ParagraphText extends ParagraphText_Base {

	public ParagraphText(TextPortion parent) {
		parent.addChildText(this);
	}

	@Override
	public void accept(TextPortionVisitor visitor) {
		visitor.visit(this);
	}

}
