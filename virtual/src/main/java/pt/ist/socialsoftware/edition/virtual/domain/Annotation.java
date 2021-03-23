package pt.ist.socialsoftware.edition.virtual.domain;

import org.apache.commons.lang.StringEscapeUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;


public abstract class Annotation extends Annotation_Base {

    @Override
    public String getText() {
        if (super.getText() == null) {
            return super.getText();
        } else {
            return StringEscapeUtils.escapeHtml(super.getText());
        }
    }

    // protected, maybe
    public void init(VirtualEditionInter inter, String quote, String text) {
        setVirtualEditionInter(inter);
        setQuote(quote);
        setText(text);
    }

    @Atomic(mode = TxMode.WRITE)
    public void remove() {
        setUser(null);

        for (Range range : getRangeSet()) {
            range.remove();
        }
        setVirtualEditionInter(null);

        deleteDomainObject();
    }

    public abstract boolean isAwareAnnotation();

    public abstract boolean isHumanAnnotation();

}
