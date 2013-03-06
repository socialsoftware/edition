package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class FormatText extends FormatText_Base {

	public FormatText() {
		super();
	}

	@Override
	public void print() {
		System.out.println("FORMAT ");
	}

	@Override
	public void accept(GraphVisitor visitor) {
		// TODO Auto-generated method stub

	}

}
