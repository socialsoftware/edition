package pt.ist.socialsoftware.edition.ldod.domain;

import org.apache.commons.lang.StringEscapeUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import java.util.List;
import java.util.stream.Collectors;

public class HumanAnnotation extends HumanAnnotation_Base {

    @Override
    public void setText(String text) {
        String escapedText = null;
        if (text == null || text.trim().equals("")) {
            escapedText = null;
        } else {
            escapedText = StringEscapeUtils.escapeHtml(text);
        }
        super.setText(escapedText);
    }

    public HumanAnnotation(VirtualEditionInter inter, SimpleText startText, SimpleText endText, String quote,
                           String text, String user) {
        super.init(inter, quote, text);
        // setVirtualEditionInter(inter);
        setStartText(startText);
        setEndText(endText);
        // setQuote(quote);
        // setTextModule(text);
        setUser(user);
    }

    @Override
    @Atomic(mode = TxMode.WRITE)
    public void remove() {
        setStartText(null);
        setEndText(null);
        for (Tag tag : getTagSet()) {
            tag.remove();
        }
        super.remove();
    }

    @Atomic(mode = TxMode.WRITE)
    public void update(String text, List<String> tags) {
        setText(text);
        getVirtualEditionInter().updateTags(this, tags);
    }

    public List<Category> getCategories() {
        return getTagSet().stream().map(t -> t.getCategory()).sorted((c1, c2) -> c1.getName().compareTo(c2.getName()))
                .collect(Collectors.toList());
    }

    public static boolean canCreate(VirtualEdition virtualEdition, String user) {
        return virtualEdition.getTaxonomy().canManipulateAnnotation(user);
    }

    public boolean canUpdate(String user) {
        return getVirtualEditionInter().getVirtualEdition().getParticipantSet().contains(user) && getUser().equals(user);
    }

    public boolean canDelete(String user) {
        return getUser().equals(user);
    }

    public boolean existsTag(String tag, VirtualEdition virtualEdition) {
        return getTagSet().stream().anyMatch(t -> t.getCategory().getName().equals(tag)
                && t.getCategory().getTaxonomy().getEdition() == virtualEdition);
    }

    @Override
    public boolean isAwareAnnotation() {
        return false;
    }

    @Override
    public boolean isHumanAnnotation() {
        return true;
    }

}
