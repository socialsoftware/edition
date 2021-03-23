package pt.ist.socialsoftware.edition.text.domain;

import pt.ist.socialsoftware.edition.text.feature.generators.TextPortionVisitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RdgGrpText extends RdgGrpText_Base {

    public RdgGrpText(TextPortion parent, TextPortion.VariationType type) {
        parent.addChildText(this);
        setType(type);
    }

    @Override
    protected TextPortion getNextChildText(ScholarInter inter) {
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
    public void putAppTextWithVariations(List<AppText> apps, List<ScholarInter> inters) {
        List<ScholarInter> newInters = new ArrayList<>(inters);
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
