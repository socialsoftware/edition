package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class FormatText extends FormatText_Base {

	public enum Rendition {
		RIGHT, LEFT, CENTER, BOLD, RED, UNDERLINED;
	};

	public FormatText() {
		super();
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

}
