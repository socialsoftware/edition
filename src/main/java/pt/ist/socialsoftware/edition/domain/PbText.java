package pt.ist.socialsoftware.edition.domain;

import java.util.Set;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class PbText extends PbText_Base {

	public PbText(TextPortion parent, Set<FragInter> interps) {
		parent.addChildText(this);
		setSurface(null);

		for (FragInter inter : interps) {
			addFragInter(inter);
		}
	}

	@Override
	public Set<FragInter> getInterps() {
		return getFragInterSet();
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void remove() {
		setSurface(null);

		for (FragInter inter : getFragInterSet()) {
			removeFragInter(inter);
		}

		super.remove();
	}

}
