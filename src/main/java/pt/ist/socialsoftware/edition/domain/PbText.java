package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class PbText extends PbText_Base {

	public PbText() {
		super();
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String writeHtml() {
		return "<hr size=\"3\" color=\"black\">";
	}

}
