package pt.ist.socialsoftware.edition.ldod.api.ldod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.Tag;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.Set;

public class LdoDInterface {
    private static final Logger logger = LoggerFactory.getLogger(LdoDInterface.class);

    public void notifyEvent(Event event) {

        if (event.getType().equals(Event.EventType.FRAG_INTER_REMOVE)) {

            LdoD.getInstance().getVirtualEditionsSet().stream()
                    .flatMap(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream())
                    .filter(virtualEditionInter -> virtualEditionInter.getUsesFragInter() != null && virtualEditionInter.getUsesFragInter().equals(event.getIdentifier()))
                    .forEach(this::removeAll);
        }
    }

    private void removeAll(VirtualEditionInter vei) {
        for (VirtualEditionInter vi : vei.getIsUsedBySet()) {
            removeAll(vi);
        }
        vei.remove();
    }

    public Set<Tag> getTagsForVei(String xmlID) {
        return LdoD.getInstance().getVirtualEditionInterByXmlId(xmlID).getTagSet();
    }

    public boolean isInterInVirtualEdition(String xmlId, String acronym) {
        return LdoD.getInstance().getVirtualEdition(acronym).getAllDepthVirtualEditionInters().stream()
                .anyMatch(virtualEditionInter -> virtualEditionInter.getXmlId().equals(xmlId));
    }

    public String getAcronymWithVeiId(String xmlId) {
        return LdoD.getInstance().getVirtualEditionsSet().stream().filter(virtualEdition -> virtualEdition.getFragInterByXmlId(xmlId) != null)
                .map(VirtualEdition::getAcronym).findFirst().orElseThrow(LdoDException::new);
    }

    public VirtualEdition getVirtualEdition(String acronym) {
        return LdoD.getInstance().getVirtualEdition(acronym);
    }
}
