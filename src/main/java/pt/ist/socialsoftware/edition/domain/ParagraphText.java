package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class ParagraphText extends ParagraphText_Base {

	public ParagraphText() {
		super();
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

}
