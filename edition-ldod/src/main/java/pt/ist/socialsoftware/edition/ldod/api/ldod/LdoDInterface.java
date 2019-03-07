package pt.ist.socialsoftware.edition.ldod.api.ldod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class LdoDInterface {
    private static Logger logger = LoggerFactory.getLogger(LdoDInterface.class);

    public void notifyEvent(Event event) {

        if (event.getType().equals(Event.EventType.FRAG_INTER_REMOVE)){

            //TODO: think about when VirtualEditionInter uses a VirtualEditionInter -> see FragInter remove

            LdoD.getInstance().getVirtualEditionsSet().stream()
                    .flatMap(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream())
                    .filter(virtualEditionInter -> virtualEditionInter.getUsesFragInter() != null && virtualEditionInter.getUsesFragInter().equals(event.getIdentifier()))
                    .forEach(virtualEditionInter1 -> {
                        logger.debug("Calling remove on id " + virtualEditionInter1.getXmlId());
                        removeAll(virtualEditionInter1);
                    });
        }
    }

    private void removeAll(VirtualEditionInter vei) {
        for (VirtualEditionInter vi : vei.getIsUsedBySet())
            removeAll(vi); // ask if this approach is the best for this relation
        vei.remove();
    }
}
