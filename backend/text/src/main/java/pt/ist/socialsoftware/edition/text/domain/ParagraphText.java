package pt.ist.socialsoftware.edition.text.domain;

import pt.ist.socialsoftware.edition.text.feature.generators.TextPortionVisitor;

public class ParagraphText extends ParagraphText_Base {

    public ParagraphText(TextPortion parent) {
        parent.addChildText(this);
    }

    @Override
    public void accept(TextPortionVisitor visitor) {
        visitor.visit(this);
    }

}
