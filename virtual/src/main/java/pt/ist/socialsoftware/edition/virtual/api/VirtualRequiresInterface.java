package pt.ist.socialsoftware.edition.virtual.api;


import pt.ist.socialsoftware.edition.notification.event.Event;

import pt.ist.socialsoftware.edition.notification.event.EventInterface;
import pt.ist.socialsoftware.edition.notification.event.SubscribeInterface;
import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.text.api.dto.CitationDto;
import pt.ist.socialsoftware.edition.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.virtual.domain.HumanAnnotation;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;

import java.util.List;
import java.util.Set;

public class VirtualRequiresInterface implements SubscribeInterface {

    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    private static VirtualRequiresInterface instance;

    public static VirtualRequiresInterface getInstance() {
        if (instance == null) {
            instance = new VirtualRequiresInterface();
        }
        return instance;
    }

    protected VirtualRequiresInterface() {
        EventInterface.getInstance().subscribe(this);
    }

    // Requires asynchronous events
    public void notify(Event event) {
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
        } else if (event.getType().equals(Event.EventType.SIMPLE_TEXT_REMOVE)){

            VirtualModule.getInstance().getVirtualEditionsSet().stream()
                    .flatMap(virtualEdition -> virtualEdition.getAllDepthVirtualEditionInters().stream())
                    .flatMap(virtualEditionInter -> virtualEditionInter.getAnnotationSet().stream())
                    .filter(annotation -> annotation.isHumanAnnotation() && (((HumanAnnotation) annotation).getStartText() != null || ((HumanAnnotation) annotation).getEndText() != null ))
                    .filter(annotation -> (((HumanAnnotation) annotation).getStartText().getXmlId().equals(event.getIdentifier())) || ((HumanAnnotation) annotation).getEndText().getXmlId().equals(event.getIdentifier()))
                    .forEach(annotation -> annotation.remove());
        }
    }

    private void removeAll(VirtualEditionInter vei) {
        for (VirtualEditionInter vi : vei.getIsUsedBySet()) {
            removeAll(vi);
        }
        vei.remove();
    }

    //Uses Text Module

    public Set<CitationDto> getCitationSet() {
        return this.textProvidesInterface.getCitationSet();
    }

    public CitationDto getCitationById(long id) {
        return this.textProvidesInterface.getCitationById(id);
    }

    public List<CitationDto> getCitationsWithInfoRanges() {
        return this.textProvidesInterface.getCitationsWithInfoRanges();
    }

    public ScholarInterDto getScholarInterByXmlId(String xmlId) {
        return this.textProvidesInterface.getScholarInter(xmlId);
    }

    public void cleanLucene() {
        this.textProvidesInterface.cleanLucene();
    }

    public void removeAllCitations() {
        textProvidesInterface.removeAllCitations();
    }
}
