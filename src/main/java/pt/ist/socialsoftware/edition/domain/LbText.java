package pt.ist.socialsoftware.edition.domain;

import java.util.Set;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class LbText extends LbText_Base {

	public LbText(Boolean isBreak, Boolean isHyphenated) {
		super();
		setBreakWord(isBreak);
		setHyphenated(isHyphenated);
	}

	public LbText(TextPortion parent, Boolean isBreak, Boolean isHyphenated,
			Set<FragInter> interps) {
		parent.addChildText(this);

		setBreakWord(isBreak);
		setHyphenated(isHyphenated);

		for (FragInter inter : interps) {
			addFragInter(inter);
		}
	}

	@Override
	public Set<FragInter> getInterps() {
		return getFragInterSet();
	}

	@Override
	public int getLength(FragInter inter) {
		return 0;
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);

	}

}
