package pt.ist.socialsoftware.edition.ldod.frontend.user.session;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.notification.dtos.user.UserDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.notification.event.Event;
import pt.ist.socialsoftware.edition.notification.event.EventVirtualEditionUpdate;
import pt.ist.socialsoftware.edition.notification.event.SubscribeInterface;

import java.util.List;

@Component
public class SessionRequiresInterface implements SubscribeInterface {

    private static SessionRequiresInterface instance;

    public static SessionRequiresInterface getInstance() {
        if (instance == null) {
            instance = new SessionRequiresInterface();
        }
        return instance;
    }

    protected SessionRequiresInterface() {
    }


    @JmsListener(id = "4", containerFactory = "jmsListenerContainerFactory", destination = "test-topic")
    public void listener(Event message){
        this.notify(message);
    }


    // Requires asynchronous events
    public void notify(Event event) {

        if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_REMOVE) ||
                event.getType().equals(Event.EventType.VIRTUAL_EDITION_UPDATE)) {
//            HttpSessionConfig.getSessions().values().stream()
//                    .map(FrontendSession.class::cast)
//                    .forEach(frontendSession -> {
            FrontendSession frontendSession = FrontendSession.getFrontendSession();
            if (frontendSession == null) {
                return;
            }
            if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_REMOVE)) {
                frontendSession.removeSelectedVE(event.getIdentifier());
            }
            if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_UPDATE)) {
                EventVirtualEditionUpdate eventVirtualEditionUpdate = (EventVirtualEditionUpdate) event;
                frontendSession.removeSelectedVE(eventVirtualEditionUpdate.getIdentifier());
                frontendSession.addSelectedVE(eventVirtualEditionUpdate.getNewAcronym());
            }
//            ;                   })
        }
    }

    // Uses Virtual Module
    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");
//    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://docker-virtual:8083/api");


    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/acronym/" + acronym)
                .retrieve()
                .bodyToMono(VirtualEditionDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionByAcronym(acronym);
    }

    public void removeSelectedByUser(String user, String virtualEditionAcronym) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                   .path("/removeVirtualEditionSelectedByUser")
                   .queryParam("user", user)
                   .queryParam("virtualEditionAcronym", virtualEditionAcronym)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.removeVirtualEditionSelectedByUser(user, virtualEditionAcronym);
    }

    public void addSelectedByUser(String user, String virtualEditionAcronym) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/addVirtualEditionSelectedByUser")
                    .queryParam("user", user)
                    .queryParam("virtualEditionAcronym", virtualEditionAcronym)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.addVirtualEditionSelectedByUser(user, virtualEditionAcronym);
    }

    public List<String> getSelectedVirtualEditionsByUser(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/userSelectedVirtualEditions")
                    .queryParam("username", username)
                .build())
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getSelectedVirtualEditionsByUser(username);
    }

    // Uses User Module
    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://localhost:8082/api");
//    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://docker-user:8082/api");


    public UserDto getUser(String user) {
        return webClientUser.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user")
                        .queryParam("username", user)
                        .build())
                .retrieve()
                .bodyToMono(UserDto.class)
                .blockOptional()
                .orElse(null);
        //        return this.userProvidesInterface.getUser(user);

    }

}
