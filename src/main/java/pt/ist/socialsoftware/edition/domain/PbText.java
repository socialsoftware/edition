package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class PbText extends PbText_Base {

	public PbText() {
		super();
	}

	@Override
	public void print() {
		System.out.print("PB ");
	}

	@Override
	public void accept(GraphVisitor visitor) {
		// TODO Auto-generated method stub

	}

}
