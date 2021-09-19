package pt.ist.socialsoftware.edition.game.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.game.domain.*;

import pt.ist.socialsoftware.edition.game.feature.classification.inout.WriteGamestoFile;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.TagDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.notification.event.Event;
import pt.ist.socialsoftware.edition.notification.event.EventInterface;
import pt.ist.socialsoftware.edition.notification.event.EventTagRemove;
import pt.ist.socialsoftware.edition.notification.event.SubscribeInterface;



import java.util.Set;
import java.util.stream.Collectors;

import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.VIRTUAL_SERVICE_URL;

@Component
public class GameRequiresInterface implements SubscribeInterface {
    private static final Logger logger = LoggerFactory.getLogger(GameRequiresInterface.class);

    private static GameRequiresInterface instance;

    public static GameRequiresInterface getInstance() {
        if (instance == null) {
            instance = new GameRequiresInterface();
        }
        return instance;
    }

    @JmsListener(id = "1", containerFactory = "jmsListenerContainerFactory", destination = "test-topic")
    public void listener(Event message){
        this.notify(message);
    }

    protected GameRequiresInterface() {
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public void notify(Event event) {

        if (event.getType().equals(Event.EventType.USER_REMOVE)) {
            String username = event.getIdentifier();

            ClassificationModule.getInstance().getPlayerSet().stream().filter(player -> player.getUser().equals(username))
                    .forEach(Player::remove);
            ClassificationModule.getInstance().getClassificationGameSet().stream()
                    .filter(classificationGame -> classificationGame.getResponsible().equals(username))
                    .forEach(ClassificationGame::remove);
        }
        else if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_REMOVE)){
            String editionId = event.getIdentifier();

            ClassificationModule.getInstance().getClassificationGameSet().stream()
                    .filter(classificationGame -> classificationGame.getEditionId().equals(editionId))
                    .forEach(ClassificationGame::remove);
        }
        else if (event.getType().equals(Event.EventType.VIRTUAL_INTER_REMOVE)){
            String interId = event.getIdentifier();

            ClassificationModule.getInstance().getClassificationGameSet().stream()
                    .filter(classificationGame -> classificationGame.getInterId().equals(interId))
                    .forEach(ClassificationGame::remove);
        }

        else if (event.getType().equals(Event.EventType.TAG_REMOVE)){
            EventTagRemove tagRemove = (EventTagRemove) event;

            String urlId = tagRemove.getIdentifier();
            String interId = tagRemove.getInterId();

            ClassificationModule.getInstance().getClassificationGameSet().stream()
                    .filter(classificationGame -> classificationGame.getTagId() != null
                            && classificationGame.getTagId().equals(urlId)
                            && classificationGame.getInterId().equals(interId))
                    .forEach(ClassificationGame::remove);
        }
        else if (event.getType().equals(Event.EventType.VIRTUAL_EXPORT)){
            WriteGamestoFile writeGamestoFile = new WriteGamestoFile();
            writeGamestoFile.exportToVirtualZip(event.getIdentifier());
        }
    }

    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl(VIRTUAL_SERVICE_URL);

    public void removeTag(String externalId){
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/removeTagFromInter")
                    .queryParam("externalId", externalId)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.removeTagFromInter(externalId);
    }

    public VirtualEditionDto getVirtualEdition(String acronym){
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym)
                .retrieve()
                .bodyToMono(VirtualEditionDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEdition(acronym);
    }

    public VirtualEditionInterDto getVirtualEditionInter(String xmlId){
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/xmlId/" + xmlId)
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionInter(xmlId);
    }

    public Set<VirtualEditionDto> getVirtualEditionUserIsParticipant(String username){
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEditionsInters/getVirtualEditionIntersUserIsContributor")
                    .queryParam("username", username)
                    .build())
                .retrieve()
                .bodyToFlux(VirtualEditionDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getVirtualEditionsUserIsParticipant(username);
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterDtoSet(String acronym){
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInterSet/" + acronym)
                .retrieve()
                .bodyToFlux(VirtualEditionInterDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getVirtualEditionInterSet(acronym);
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterDtoSet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInterSet")
                .retrieve()
                .bodyToFlux(VirtualEditionInterDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getVirtualEditionInterSet(acronym);
    }

    public boolean getOpenVocabularyStatus(String acronym){
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/taxonomyVocabularyStatus")
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //        return this.virtualProvidesInterface.getVirtualEditionTaxonomyVocabularyStatus(acronym);
    }

    public boolean getOpenAnnotationStatus(String acronym){
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/taxonomyAnnotationStatus")
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //        return this.virtualProvidesInterface.getVirtualEditionTaxonomyAnnotationStatus(acronym);
    }

    public TagDto createTag(String editionId, String interId, String tagName, String user){
        return webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/createTagInInter")
                    .queryParam("editionId", editionId)
                    .queryParam("interId", interId)
                    .queryParam("tagName", tagName)
                    .queryParam("user", user)
                .build())
                .retrieve()
                .bodyToMono(TagDto.class)
                .block();
        //        return this.virtualProvidesInterface.createTagInInter(editionId, interId, tagName, user);
    }

    public boolean isUserParticipant(String acronym, String username){
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + acronym + "/isUserParticipant")
                    .queryParam("username", username)
                    .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //        return this.virtualProvidesInterface.isUserParticipant(acronym, username);
    }

    public VirtualEditionInterDto getVirtualEditionInterFromModule(String xmlId) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/" + xmlId + "/interFromModule")
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
    }
}
