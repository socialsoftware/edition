package pt.ist.socialsoftware.edition.ldod.api.ldod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.Set;

public class LdoDInterface {
    private static Logger logger = LoggerFactory.getLogger(LdoDInterface.class);

    public void notifyEvent(Event event) {

        if (event.getType().equals(Event.EventType.FRAG_INTER_REMOVE)){

            LdoD.getInstance().getVirtualEditionsSet().stream()
                    .flatMap(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream())
                    .filter(virtualEditionInter -> virtualEditionInter.getUsesFragInter() != null && virtualEditionInter.getUsesFragInter().equals(event.getIdentifier()))
                    .forEach(this::removeAll);
        }
    }

    private void removeAll(VirtualEditionInter vei) {
        for (VirtualEditionInter vi : vei.getIsUsedBySet())
            removeAll(vi); // ask if this approach is the best for this relation
        vei.remove();
    }

    public Set<Tag> getTagsForVei(String xmlID) {
        return LdoD.getInstance().getVirtualEditionInterByXmlId(xmlID).getTagSet();
    }

    public boolean isVirtualEditionInUserSelectionSet(String xmlId){
        String acronym = LdoD.getInstance().getVirtualEditionsSet().stream()
                .filter(virtualEdition -> virtualEdition.getFragInterByXmlId(xmlId) != null)
                .map(virtualEdition -> virtualEdition.getAcronym()).findFirst().orElseThrow(LdoDException::new);
        return LdoDUser.getAuthenticatedUser().getSelectedVirtualEditionsSet().stream()
                .map(virtualEdition -> virtualEdition.getAcronym()).anyMatch(s -> s.equals(acronym));
    }

    public boolean isInterInVirtualEdition(String xmlId, String acronym) {
        return LdoD.getInstance().getVirtualEdition(acronym).getAllDepthVirtualEditionInters().stream()
                .anyMatch(virtualEditionInter -> virtualEditionInter.getXmlId().equals(xmlId));
    }
}
