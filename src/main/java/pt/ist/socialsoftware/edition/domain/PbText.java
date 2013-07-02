package pt.ist.socialsoftware.edition.domain;

import java.util.Set;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class PbText extends PbText_Base {

	public PbText(TextPortion parent, Set<FragInter> interps) {
		parent.addChildText(this);

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
