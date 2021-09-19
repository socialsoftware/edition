package pt.ist.socialsoftware.edition.text.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.notification.event.Event;
import pt.ist.socialsoftware.edition.text.config.BeanUtil;
import pt.ist.socialsoftware.edition.text.api.TextEventPublisher;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ScholarInter extends ScholarInter_Base {
    private static final Logger logger = LoggerFactory.getLogger(ScholarInter.class);

    public String getUrlId() {
        return getXmlId().replace(".", "_");
    }

    public abstract String getShortName();

    public abstract int getNumber();

    public abstract String getTitle();

    public abstract ScholarEdition getEdition();

    public abstract boolean isExpertInter();

    public abstract ScholarInter getLastUsed();

    public abstract String getReference();

    public void remove() {

//        EventInterface.getInstance().publish(new Event(Event.EventType.SCHOLAR_INTER_REMOVE, this.getXmlId()));
        TextEventPublisher eventPublisher = BeanUtil.getBean(TextEventPublisher.class);
        eventPublisher.publishEvent(new Event(Event.EventType.SCHOLAR_INTER_REMOVE, this.getXmlId()));

        setFragment(null);
        setHeteronym(null);

        if (getLdoDDate() != null) {
            getLdoDDate().remove();
        }

        for (RdgText rdg : getRdgSet()) {
            removeRdg(rdg);
        }

        for (LbText lb : getLbTextSet()) {
            removeLbText(lb);
        }

        for (PbText pb : getPbTextSet()) {
            removePbText(pb);
        }

        for (AnnexNote annexNote : getAnnexNoteSet()) {
            annexNote.remove();
        }

        for (RefText ref : getRefTextSet()) {
            ref.setScholarInter(null);
        }

        // TODO: it does not belong to this model
        getInfoRangeSet().forEach(infoRange -> infoRange.remove());

        deleteDomainObject();
    }

    public List<AnnexNote> getSortedAnnexNote() {
        List<AnnexNote> results = new ArrayList<>(getAnnexNoteSet());

        Collections.sort(results);

        return results;
    }

    public long getNumberOfTwitterCitationsSince(LocalDateTime editionBeginDateTime) {
        return getInfoRangeSet().stream().map(ir -> ir.getCitation())
                .filter(cit -> cit.getFormatedDate().isAfter(editionBeginDateTime)).count();
    }

}
