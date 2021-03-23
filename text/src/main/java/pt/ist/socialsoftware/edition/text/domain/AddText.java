package pt.ist.socialsoftware.edition.text.domain;

import pt.ist.socialsoftware.edition.text.feature.generators.TextPortionVisitor;

public class AddText extends AddText_Base {

    public enum Place {
        ABOVE("above"), BELOW("below"), SUPERIMPOSED("superimposed"), MARGIN("margin"), TOP("top"), BOTTOM(
                "bottom"), INLINE("inline"), INSPACE(
                "inspace"), OVERLEAF("overleaf"), OPPOSITE("opposite"), END("end"), UNSPECIFIED("unspecified");

        private final String desc;

        Place(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return this.desc;
        }
    }

	public AddText(TextPortion parent, Place place) {
        parent.addChildText(this);
        setPlace(place);
    }

    @Override
    public void accept(TextPortionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Boolean isFormat(Boolean displayDel, Boolean highlightSubst, ScholarInter fragInter) {
        if (getInterps().contains(fragInter)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getNote() {
        String result = "Adicionado - " + getPlace().toString();

        return result;
    }

}
