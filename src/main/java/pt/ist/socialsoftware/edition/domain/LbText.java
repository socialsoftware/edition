package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class LbText extends LbText_Base {

	public LbText(Boolean isBreak, Boolean isHyphenated) {
		super();
		setBreakWord(isBreak);
		setHyphenated(isHyphenated);
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);

	}

}
