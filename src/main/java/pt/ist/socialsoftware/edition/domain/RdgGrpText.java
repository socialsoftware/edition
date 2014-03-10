package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class RdgGrpText extends RdgGrpText_Base {

	public RdgGrpText(TextPortion parent, VariationType type) {
		parent.addChildText(this);
		setType(type);
	}

	@Override
	public TextPortion getNextDepthFirstText(FragInter inter) {
		// check child
		if (this.getInterps().contains(inter)) {
			for (TextPortion childText : getChildTextSet()) {
				if (childText.getInterps().contains(inter)) {
					return childText;
				}
			}
		}

		// not necessary to check next because an inter applies to a
		// single rdgGrp

		// check next of parent
		return getBacktrackingNextOfParentText(inter);
	}

	// optimizes the superclass method
	@Override
	protected TextPortion getBacktrackingNextOfParentText(FragInter inter) {
		TextPortion parent = getParentText();
		if (parent == null) {
			return null;
		} else {
			return parent.getBacktrackingNextOfParentText(inter);
		}
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public boolean hasVariations(List<FragInter> inters) {
		Set<FragInter> intersection = new HashSet<FragInter>(inters);
		intersection.retainAll(getInterps());
		if (!intersection.isEmpty() && !getInterps().containsAll(inters)) {
			return true;
		} else {
			boolean hasVariations = false;
			for (TextPortion text : getChildTextSet()) {
				if (text.hasVariations(inters)) {
					hasVariations = true;
					break;
				}
			}
			return hasVariations;
		}
	}

}
