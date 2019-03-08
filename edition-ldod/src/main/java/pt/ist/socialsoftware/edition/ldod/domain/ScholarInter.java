package pt.ist.socialsoftware.edition.ldod.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.api.event.EventInterface;
import pt.ist.socialsoftware.edition.ldod.api.ldod.LdoDInterface;

public abstract class ScholarInter extends ScholarInter_Base {
    private static Logger logger = LoggerFactory.getLogger(ScholarInter.class);

    @Override
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
            ref.setFragInter(null);
        }

        // adicionado recentemente, testar
        getInfoRangeSet().forEach(infoRange -> infoRange.remove());

        deleteDomainObject();
    }
}
