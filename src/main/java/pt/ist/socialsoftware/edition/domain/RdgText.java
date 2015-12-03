package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.socialsoftware.edition.visitors.TextPortionVisitor;

public class RdgText extends RdgText_Base implements GraphElement {

	public RdgText(TextPortion parent, VariationType type,
			Set<FragInter> fragInters) {
		parent.addChildText(this);
		setType(type);

		for (FragInter inter : fragInters) {
			addFragInters(inter);
		}

	}

	@Override
	public Set<FragInter> getInterps() {
		return getFragIntersSet();
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
	public Boolean isFormat(Boolean displayDel, Boolean highlightSubst,
			FragInter fragInter) {
		return true;
	}

	@Override
	public RdgText getClosestRdg() {
		return this;
	}

	public void removeOnlyThis() {

		for (FragInter fragInter : getFragIntersSet()) {
			removeFragInters(fragInter);
		}

		deleteDomainObject();
	}

	@Override
	public void remove() {
		for (FragInter inter : getFragIntersSet()) {
			removeFragInters(inter);
		}

		super.remove();
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
