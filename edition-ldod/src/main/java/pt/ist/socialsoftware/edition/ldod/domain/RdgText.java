package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.socialsoftware.edition.ldod.domain.RdgText_Base;
import pt.ist.socialsoftware.edition.ldod.generators.TextPortionVisitor;

public class RdgText extends RdgText_Base implements GraphElement {

	public RdgText(TextPortion parent, TextPortion.VariationType type, Set<ScholarInter> fragInters) {
		parent.addChildText(this);
		setType(type);

		for (ScholarInter inter : fragInters) {
			addScholarInter(inter);
		}

	}

	@Override
	public Set<ScholarInter> getInterps() {
		return getScholarInterSet();
	}

	@Override
	protected TextPortion getNextSibilingText(ScholarInter inter) {
		return null;
	}

	@Override
	protected TextPortion getNextParentText(ScholarInter inter) {
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
	public Boolean isFormat(Boolean displayDel, Boolean highlightSubst, ScholarInter fragInter) {
		return true;
	}

	@Override
	public RdgText getClosestRdg() {
		return this;
	}

	public void removeOnlyThis() {

		for (ScholarInter fragInter : getScholarInterSet()) {
			removeScholarInter(fragInter);
		}

		deleteDomainObject();
	}

	@Override
	public void remove() {
		for (ScholarInter inter : getScholarInterSet()) {
			removeScholarInter(inter);
		}

		super.remove();
	}

	@Override
	public void putAppTextWithVariations(List<AppText> apps, List<ScholarInter> inters) {
		List<ScholarInter> newInters = new ArrayList<>(inters);
		newInters.retainAll(inters);
		super.putAppTextWithVariations(apps, newInters);
	}

	@Override
	public boolean hasVariations(List<ScholarInter> inters) {
		Set<ScholarInter> intersection = new HashSet<>(inters);
		intersection.retainAll(getInterps());
		if (!intersection.isEmpty() && !getInterps().containsAll(inters)) {
			return true;
		}
		return false;
	}
}
