package pt.ist.socialsoftware.edition.core.domain;

import java.util.Set;

import pt.ist.socialsoftware.edition.core.domain.LbText_Base;
import pt.ist.socialsoftware.edition.core.generators.TextPortionVisitor;

public class LbText extends LbText_Base {

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
	public void accept(TextPortionVisitor visitor) {
		visitor.visit(this);

	}

	@Override
	public void remove() {
		for (FragInter inter : getFragInterSet()) {
			removeFragInter(inter);
		}

		super.remove();
	}
}
