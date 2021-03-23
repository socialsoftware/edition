package pt.ist.socialsoftware.edition.virtual.domain;

import org.apache.commons.lang.StringEscapeUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.text.api.dto.SimpleTextDto;

import java.util.List;
import java.util.stream.Collectors;

public class HumanAnnotation extends HumanAnnotation_Base {

    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    private String startTextId;
    private String endTextId;

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

    public HumanAnnotation(VirtualEditionInter inter, SimpleTextDto startText, SimpleTextDto endText, String quote,
                           String text, String user) {
        super.init(inter, quote, text);
        // setVirtualEditionInter(inter);

//        setStartText(startText);
//        setEndText(endText);

        if (startText != null && endText != null) {
            this.startTextId = startText.getExternalId();
            this.endTextId = endText.getExternalId();
        }

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

    public void setStartText(String externalId) {
        this.startTextId = externalId;
    }

    public void setEndText(String externalId) {
        this.endTextId = externalId;
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

    public SimpleTextDto getStartText() {
        return this.textProvidesInterface.getSimpleTextFromExternalId(this.startTextId);
    }

    public SimpleTextDto getEndText() {
        return this.textProvidesInterface.getSimpleTextFromExternalId(this.endTextId);
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
