package pt.ist.socialsoftware.edition.text.domain;

import pt.ist.socialsoftware.edition.text.feature.generators.TextPortionVisitor;

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
							ScholarInter fragInter) {
        if (getInterps().contains(fragInter) && highlightSubst) {
            return true;
        } else {
            return false;
        }
    }

}
