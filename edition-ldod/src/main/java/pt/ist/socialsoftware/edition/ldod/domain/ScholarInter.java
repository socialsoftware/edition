package pt.ist.socialsoftware.edition.ldod.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.api.event.EventInterface;

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

    public abstract Edition getEdition();

    public abstract Edition.EditionType getSourceType();

    public abstract boolean belongs2Edition(Edition edition);

    public abstract ScholarInter getLastUsed();

    public void remove() {

        setFragment(null);
        setHeteronym(null);

        if (getLdoDDate() != null) {
            getLdoDDate().remove();
        }

        EventInterface eventInterface = new EventInterface();
        eventInterface.publish(new Event(Event.EventType.FRAG_INTER_REMOVE, this.getXmlId()));

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

        // adicionado recentemente, testar
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
