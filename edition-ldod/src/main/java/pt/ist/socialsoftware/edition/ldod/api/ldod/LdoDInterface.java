package pt.ist.socialsoftware.edition.ldod.api.ldod;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class LdoDInterface {


    @Atomic(mode = Atomic.TxMode.WRITE)
    public void notifyEvent(Event event) {

        if (event.getType().equals(Event.EventType.FRAG_INTER_REMOVE)){

            System.out.println("Sending event for removal of frag inter " + event.getIdentifier());

            LdoD.getInstance().getVirtualEditionsSet().stream()
                    .flatMap(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream())
                    .filter(virtualEditionInter -> virtualEditionInter.getXmlId().equals(event.getIdentifier()))
                    .forEach(VirtualEditionInter::remove);

            System.out.println("Event sent!");
        }
    }
}
