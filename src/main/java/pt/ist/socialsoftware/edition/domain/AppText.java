package pt.ist.socialsoftware.edition.domain;

import java.util.List;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class AppText extends AppText_Base {

	public AppText(TextPortion parent, VariationType type) {
		if (parent != null) {
			parent.addChildText(this);
		}

		setType(type);
	}

	public AppText(VariationType type) {
		setParentText(null);

		setType(type);
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
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

		// check next
		TextPortion nextText = getNextText();
		if (nextText != null) {
			if (nextText.getInterps().contains(inter)) {
				return nextText;
			} else {
				return nextText.getNextDepthFirstText(inter);
			}
		}

		// check next of parent
		return getBacktrackingNextOfParentText(inter);
	}

	@Override
	public void putAppTextWithVariations(List<AppText> apps,
			List<FragInter> inters) {

		if (hasVariations(inters)) {
			apps.add(this);
		}
	}

	@Override
	public boolean hasVariations(List<FragInter> inters) {
		for (TextPortion text : getChildTextSet()) {
			if (text.hasVariations(inters)) {
				return true;
			}
		}
		return false;
	}

}
