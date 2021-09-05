package pt.ist.socialsoftware.edition.text.domain;

import pt.ist.socialsoftware.edition.text.feature.generators.TextPortionVisitor;

import java.util.Set;

public class PbText extends PbText_Base {

    public PbText(TextPortion parent, Set<ScholarInter> interps, int pbOrder) {
        parent.addChildText(this);
        setSurface(null);
        setOrder(pbOrder);

        for (ScholarInter inter : interps) {
            addScholarInter(inter);
        }
    }

    @Override
    public Set<ScholarInter> getInterps() {
        return getScholarInterSet();
    }

    @Override
    public void accept(TextPortionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void remove() {
        setSurface(null);

        for (ScholarInter inter : getScholarInterSet()) {
            removeScholarInter(inter);
        }

        super.remove();
    }

    public PbText getPrevPbText(ScholarInter inter) {
        PbText prevPbText = null;
        for (PbText pbText : inter.getPbTextSet()) {
            if ((pbText.getOrder() < this.getOrder())
                    && ((prevPbText == null) || (pbText.getOrder() > prevPbText
                    .getOrder()))) {
                prevPbText = pbText;
            }
        }
        return prevPbText;
    }

    public PbText getNextPbText(ScholarInter inter) {
        PbText nextPbText = null;
        for (PbText pbText : inter.getPbTextSet()) {
            if ((pbText.getOrder() > this.getOrder())
                    && ((nextPbText == null) || (pbText.getOrder() < nextPbText
                    .getOrder()))) {
                nextPbText = pbText;
            }
        }
        return nextPbText;
    }
}
