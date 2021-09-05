package pt.ist.socialsoftware.edition.text.domain;


import pt.ist.socialsoftware.edition.text.feature.generators.TextPortionVisitor;

import java.util.List;

public class AltText extends AltText_Base {

    public enum AltMode {
        EXCL("excl"), INCL("incl"), NONSPECIFIED("nonspecified");

        private final String desc;

        AltMode(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return this.desc;
        }
    }

	public AltText(TextPortion parent, SegText segTextOne, SegText segTextTwo,
				   AltMode mode, double weightOne, double weightTwo) {
        parent.addChildText(this);
    }

    public AltText(TextPortion parent, List<SegText> segTextList, AltMode mode,
				   String[] weightList) {
        parent.addChildText(this);
        setMode(mode);

        int i = 0;
        for (SegText segText : segTextList) {
            double weight = Double.parseDouble(weightList[i]);
            addAltTextWeight(new AltTextWeight(segText, weight));
            i++;
        }
    }

    @Override
    public void accept(TextPortionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void remove() {
        for (AltTextWeight weight : getAltTextWeightSet()) {
            weight.remove();
        }
        super.remove();
    }

}
