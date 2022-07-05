package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.Set;

import pt.ist.socialsoftware.edition.ldod.generators.TextPortionVisitor;
import pt.ist.socialsoftware.edition.ldod.domain.PbText_Base;

public class PbText extends PbText_Base {

	public PbText(TextPortion parent, Set<FragInter> interps, int pbOrder) {
		parent.addChildText(this);
		setSurface(null);
		setOrder(pbOrder);

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
		setSurface(null);

		for (FragInter inter : getFragInterSet()) {
			removeFragInter(inter);
		}

		super.remove();
	}

	public PbText getPrevPbText(FragInter inter) {
		PbText prevPbText = null;
		for (PbText pbText : inter.getPbTextSet()) {
			if ((pbText.getOrder() < this.getOrder())
					&& ((prevPbText == null) || (pbText.getOrder() > prevPbText
							.getOrder()))) {
				prevPbText = pbText;
			}
		}
		return prevPbText;
	}

	public PbText getNextPbText(FragInter inter) {
		PbText nextPbText = null;
		for (PbText pbText : inter.getPbTextSet()) {
			if ((pbText.getOrder() > this.getOrder())
					&& ((nextPbText == null) || (pbText.getOrder() < nextPbText
							.getOrder()))) {
				nextPbText = pbText;
			}
		}
		return nextPbText;
	}
}
