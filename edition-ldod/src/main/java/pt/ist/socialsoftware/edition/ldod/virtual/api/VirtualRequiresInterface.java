package pt.ist.socialsoftware.edition.ldod.virtual.api;

import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;

public class VirtualRequiresInterface {
    // Requires asynchronous events
    public void notifyEvent(Event event) {
        if (event.getType().equals(Event.EventType.FRAGMENT_REMOVE)) {
            VirtualModule.getInstance().getVirtualEditionsSet().stream()
                    .flatMap(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream())
                    .filter(virtualEditionInter -> virtualEditionInter.getUsesScholarInterId() != null && virtualEditionInter.getFragmentXmlId().equals(event.getIdentifier()))
                    .forEach(this::removeAll);
        } else if (event.getType().equals(Event.EventType.SCHOLAR_INTER_REMOVE)) {
            VirtualModule.getInstance().getVirtualEditionsSet().stream()
                    .flatMap(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream())
                    .filter(virtualEditionInter -> virtualEditionInter.getUsesScholarInterId() != null && virtualEditionInter.getUsesScholarInterId().equals(event.getIdentifier()))
                    .forEach(this::removeAll);
        } else if (event.getType().equals(Event.EventType.USER_REMOVE)) {
            String username = event.getIdentifier();
            VirtualModule virtualModule = VirtualModule.getInstance();

            virtualModule.getVirtualEditionsSet().stream().flatMap(virtualEdition -> virtualEdition.getMemberSet().stream())
                    .filter(member -> member.getUser().equals(username))
                    .forEach(member -> member.remove());
            virtualModule.getVirtualEditionsSet().stream().flatMap(virtualEdition -> virtualEdition.getSelectedBySet().stream())
                    .filter(selectedBy -> selectedBy.getUser().equals(username))
                    .forEach(selectedBy -> selectedBy.remove());
            virtualModule.getVirtualEditionsSet().stream().flatMap(virtualEdition -> virtualEdition.getTaxonomy().getCategoriesSet().stream())
                    .flatMap(category -> category.getTagSet().stream())
                    .filter(tag -> tag.getContributor().equals(username))
                    .forEach(tag -> tag.remove());
            virtualModule.getVirtualEditionsSet().stream().flatMap(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream())
                    .flatMap(virtualEditionInter -> virtualEditionInter.getAnnotationSet().stream())
                    .filter(annotation -> annotation.getUser().equals(username))
                    .forEach(annotation -> annotation.remove());
        }
    }

    private void removeAll(VirtualEditionInter vei) {
        for (VirtualEditionInter vi : vei.getIsUsedBySet()) {
            removeAll(vi);
        }
        vei.remove();
    }

    // Requires from User Module
    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    public String getAuthenticatedUser() {
        return this.userProvidesInterface.getAuthenticatedUser();
    }


}
