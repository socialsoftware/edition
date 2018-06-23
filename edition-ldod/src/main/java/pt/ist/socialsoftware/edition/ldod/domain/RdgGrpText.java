package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.socialsoftware.edition.ldod.domain.RdgGrpText_Base;
import pt.ist.socialsoftware.edition.ldod.generators.TextPortionVisitor;

public class RdgGrpText extends RdgGrpText_Base {

	public RdgGrpText(TextPortion parent, TextPortion.VariationType type) {
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
	public void accept(TextPortionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void putAppTextWithVariations(List<AppText> apps, List<FragInter> inters) {
		List<FragInter> newInters = new ArrayList<>(inters);
		newInters.retainAll(inters);
		super.putAppTextWithVariations(apps, newInters);
	}

	public Set<RdgText> getChildRdgTextSet() {
		Set<RdgText> rdgs = new HashSet<>();
		for (TextPortion text : getChildTextSet()) {
			if (text instanceof RdgText) {
				rdgs.add((RdgText) text);
			} else if (text instanceof RdgGrpText) {
				rdgs.addAll(((RdgGrpText) text).getChildRdgTextSet());
			}
		}
		return rdgs;
	}

}
