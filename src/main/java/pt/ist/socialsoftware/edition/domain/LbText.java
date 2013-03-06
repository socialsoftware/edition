package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class LbText extends LbText_Base {

	public LbText() {
		super();
		setBreakWord(true);
		setHyphenated(false);
	}

	@Override
	public void print() {
		System.out.print("LB(BW:" + getBreakWord() + ",H:" + getHyphenated()
				+ ") ");
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);

	}

}
