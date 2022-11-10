package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HumanAnnotation extends HumanAnnotation_Base {

    public HumanAnnotation(VirtualEditionInter inter, SimpleText startText, SimpleText endText, String quote,
                           String text, LdoDUser user, String contents) {
        super.init(inter, quote, text);
        // setVirtualEditionInter(inter);
        setStartText(startText);
        setEndText(endText);
        setContents(contents);
        // setQuote(quote);
        // setText(text);
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
    public void update(String text, List<String> tags, String contents) {
        setText(text);
        setContents(contents);
        getVirtualEditionInter().updateTags(this, tags);
    }

    public List<Category> getCategories() {
        return getTagSet()
                .stream()
                .map(Tag_Base::getCategory)
                .sorted(Comparator.comparing(Category_Base::getName))
                .collect(Collectors.toList());
    }

    public static boolean canCreate(VirtualEdition virtualEdition, LdoDUser user) {
        return virtualEdition.getTaxonomy().canManipulateAnnotation(user);
    }

    public boolean canUpdate(LdoDUser user) {
        return getVirtualEditionInter().getVirtualEdition().getParticipantSet().contains(user) && getUser() == user;
    }

    public boolean canDelete(LdoDUser user) {
        return getUser() == user;
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
