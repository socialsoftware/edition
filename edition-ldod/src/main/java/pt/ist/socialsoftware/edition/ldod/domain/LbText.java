package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.Set;

import pt.ist.socialsoftware.edition.ldod.domain.LbText_Base;
import pt.ist.socialsoftware.edition.ldod.generators.TextPortionVisitor;

public class LbText extends LbText_Base {

	public LbText(TextPortion parent, Boolean isBreak, Boolean isHyphenated,
			Set<ScholarInter> interps) {
		parent.addChildText(this);

		setBreakWord(isBreak);
		setHyphenated(isHyphenated);

		for (ScholarInter inter : interps) {
			addScholarInter(inter);
		}
	}

	@Override
	public Set<ScholarInter> getInterps() {
		return getScholarInterSet();
	}

	@Override
	public void accept(TextPortionVisitor visitor) {
		visitor.visit(this);

	}

	@Override
	public void remove() {
		for (ScholarInter inter : getScholarInterSet()) {
			removeScholarInter(inter);
		}

		super.remove();
	}
}
