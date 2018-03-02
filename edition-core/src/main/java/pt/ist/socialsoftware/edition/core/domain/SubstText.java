package pt.ist.socialsoftware.edition.core.domain;

import pt.ist.socialsoftware.edition.core.domain.SubstText_Base;
import pt.ist.socialsoftware.edition.core.generators.TextPortionVisitor;

public class SubstText extends SubstText_Base {

	public SubstText(TextPortion parent) {
		parent.addChildText(this);
	}

	@Override
	public void accept(TextPortionVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Boolean isFormat(Boolean displayDel, Boolean highlightSubst,
			FragInter fragInter) {
		if (getInterps().contains(fragInter) && highlightSubst) {
			return true;
		} else {
			return false;
		}
	}

}
