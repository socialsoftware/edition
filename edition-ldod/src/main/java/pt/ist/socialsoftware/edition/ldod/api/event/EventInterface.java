package pt.ist.socialsoftware.edition.ldod.api.event;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.api.ldod.LdoDInterface;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;

import java.util.List;

public class EventInterface {

    @Atomic(mode = Atomic.TxMode.WRITE)
    public void publish(Event event) {

        LdoDInterface ldoDInterface = new LdoDInterface();

        ldoDInterface.notifyEvent(event);
    }
}
