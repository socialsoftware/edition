package pt.ist.socialsoftware.edition.text.domain;


import pt.ist.socialsoftware.edition.text.feature.generators.TextPortionVisitor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppText extends AppText_Base {

    public AppText(TextPortion parent, TextPortion.VariationType type) {
        if (parent != null) {
            parent.addChildText(this);
        }

        setType(type);
    }

    public AppText(TextPortion.VariationType type) {
        setParentText(null);

        setType(type);
    }

    @Override
    public void accept(TextPortionVisitor visitor) {
        visitor.visit(this);
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
    public void putAppTextWithVariations(List<AppText> apps, List<ScholarInter> inters) {
        if (hasVariations(inters) && hasNotTransitiveParent(apps)) {
            apps.add(this);
        }

        super.putAppTextWithVariations(apps, inters);
    }

    private boolean hasNotTransitiveParent(List<AppText> apps) {
        for (AppText appText : apps) {
            if (appText.isTransitiveParent(this)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean hasVariations(List<ScholarInter> inters) {
        Set<RdgText> rdgChildSet = getChildRdgTextSet();

        for (RdgText rdg : rdgChildSet) {
            if (rdg.hasVariations(inters)) {
                return true;
            }
        }

        return false;
    }

    private Set<RdgText> getChildRdgTextSet() {
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
