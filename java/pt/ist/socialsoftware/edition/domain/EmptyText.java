package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class EmptyText extends EmptyText_Base {

	public EmptyText(Boolean isBreak) {
		super();
		setIsBreak(isBreak);
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String writeHtml() {
		return "";
	}

}
