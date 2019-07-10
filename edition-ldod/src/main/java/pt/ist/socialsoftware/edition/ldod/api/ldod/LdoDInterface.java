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
        if (event.getType().equals(Event.EventType.FRAGMENT_REMOVE)) {
            LdoD.getInstance().getVirtualEditionsSet().stream()
                    .flatMap(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream())
                    .filter(virtualEditionInter -> virtualEditionInter.getUsesScholarInterId() != null && virtualEditionInter.getFragmentXmlId().equals(event.getIdentifier()))
                    .forEach(this::removeAll);
        } else if (event.getType().equals(Event.EventType.SCHOLAR_INTER_REMOVE)) {
            LdoD.getInstance().getVirtualEditionsSet().stream()
                    .flatMap(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream())
                    .filter(virtualEditionInter -> virtualEditionInter.getUsesScholarInterId() != null && virtualEditionInter.getUsesScholarInterId().equals(event.getIdentifier()))
                    .forEach(this::removeAll);
        } else if (event.getType().equals(Event.EventType.USER_REMOVE)) {
            String username = event.getIdentifier();
            LdoD ldoD = LdoD.getInstance();

            ldoD.getVirtualEditionsSet().stream().flatMap(virtualEdition -> virtualEdition.getMemberSet().stream())
                    .filter(member -> member.getUser().equals(username))
                    .forEach(member -> member.remove());
            ldoD.getVirtualEditionsSet().stream().flatMap(virtualEdition -> virtualEdition.getSelectedBySet().stream())
                    .filter(selectedBy -> selectedBy.getUser().equals(username))
                    .forEach(selectedBy -> selectedBy.remove());
            ldoD.getVirtualEditionsSet().stream().flatMap(virtualEdition -> virtualEdition.getTaxonomy().getCategoriesSet().stream())
                    .flatMap(category -> category.getTagSet().stream())
                    .filter(tag -> tag.getContributor().equals(username))
                    .forEach(tag -> tag.remove());
            ldoD.getVirtualEditionsSet().stream().flatMap(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream())
                    .flatMap(virtualEditionInter -> virtualEditionInter.getAnnotationSet().stream())
                    .filter(annotation -> annotation.getUser().equals(username))
                    .forEach(annotation -> annotation.remove());
            ldoD.getVirtualEditionsSet().stream().flatMap(virtualEdition -> virtualEdition.getRecommendationWeightsSet().stream())
                    .filter(recommendationWeights -> recommendationWeights.getUser().equals(username))
                    .forEach(recommendationWeights -> recommendationWeights.remove());
            ldoD.getPlayerSet().stream().filter(player -> player.getUser().equals(username)).forEach(player -> player.remove());
            ldoD.getVirtualEditionInterSet().stream().flatMap(virtualEditionInter -> virtualEditionInter.getClassificationGameSet().stream())
                    .filter(classificationGame -> classificationGame.getResponsible().equals(username)).forEach(classificationGame -> classificationGame.remove());
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
