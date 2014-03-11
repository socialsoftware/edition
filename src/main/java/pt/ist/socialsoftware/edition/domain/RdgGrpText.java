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
	protected TextPortion getNextChildText(FragInter inter) {
		if (this.getInterps().contains(inter)) {
			for (TextPortion childText : getChildTextSet()) {
				if (childText.getInterps().contains(inter)) {
					return childText;
				}
			}
		}
		return null;
	}

	@Override
	protected TextPortion getNextSibilingText(FragInter inter) {
		return null;
	}

	@Override
	protected TextPortion getNextParentText(FragInter inter) {
		TextPortion parentText = getParentText();
		if (parentText != null) {
			return parentText.getNextParentText(inter);
		} else {
			return null;
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
