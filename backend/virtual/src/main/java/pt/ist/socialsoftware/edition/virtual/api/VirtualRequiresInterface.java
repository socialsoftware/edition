package pt.ist.socialsoftware.edition.virtual.api;


import org.springframework.http.HttpStatus;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.notification.dtos.text.*;
import pt.ist.socialsoftware.edition.notification.dtos.user.UserDto;
import pt.ist.socialsoftware.edition.notification.event.Event;

import pt.ist.socialsoftware.edition.notification.event.EventInterface;
import pt.ist.socialsoftware.edition.notification.event.SubscribeInterface;
import pt.ist.socialsoftware.edition.virtual.domain.HumanAnnotation;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.TEXT_SERVICE_URL;
import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.USER_SERVICE_URL;

@Component
public class VirtualRequiresInterface implements SubscribeInterface {

    private static Map<String, ScholarInterDto> scholarInterMap = new HashMap<>();

    public static void cleanScholarInterMapCache() {
        scholarInterMap = new HashMap<>();
    }

    private final WebClient.Builder webClient = WebClient.builder().baseUrl(TEXT_SERVICE_URL);

    private static VirtualRequiresInterface instance;

    public static VirtualRequiresInterface getInstance() {
        if (instance == null) {
            instance = new VirtualRequiresInterface();
        }
        return instance;
    }

    protected VirtualRequiresInterface() {
    }


    @JmsListener(id = "2", containerFactory = "jmsListenerContainerFactory", destination = "test-topic")
    public void listener(Event message){
        this.notify(message);
    }


    @Atomic(mode = Atomic.TxMode.WRITE)
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
                    .filter(annotation -> annotation.isHumanAnnotation() && (((HumanAnnotation) annotation).getStartTextId().equals(event.getIdentifier())) || ((HumanAnnotation) annotation).getEndTextId().equals(event.getIdentifier()))
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
        return webClient.build()
                .get()
                .uri("/citations")
                .retrieve()
                .bodyToFlux(CitationDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //       return this.textProvidesInterface.getCitationSet();
    }

    public CitationDto getCitationById(long id) {
        return webClient.build()
                .get()
                .uri("/citations/" + id)
                .retrieve()
                .bodyToMono(CitationDto.class)
                .blockOptional().get();
        //     return this.textProvidesInterface.getCitationById(id);
    }

    public List<CitationDto> getCitationsWithInfoRanges() {
        return webClient.build()
                .get()
                .uri("/citationsInfoRanges")
                .retrieve()
                .bodyToFlux(CitationDto.class)
                .collectList()
                .block();
    }

    public ScholarInterDto getScholarInterByXmlId(String xmlId) {
//        return webClient.get()
//                .uri("/scholarInter/" + xmlId)
//                .retrieve()
//                .bodyToMono(ScholarInterDto.class)
//                .blockOptional().get();
        return getScholarInterByXmlIdCache(xmlId).orElse(null);
        //    return this.textProvidesInterface.getScholarInter(xmlId);
    }

    public ScholarInterDto getScholarInterbyExternalId(String externalId) {
        return webClient.build()
                .get()
                .uri("/scholarInter/ext/" + externalId)
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .blockOptional().get();
    }

    public void cleanLucene() {
        webClient.build()
                .get()
                .uri("/cleanLucene")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //    this.textProvidesInterface.cleanLucene();
    }

    public void removeAllCitations() {
        webClient.build()
                .delete()
                .uri("/removeAllCitations")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
       // textProvidesInterface.removeAllCitations();
    }


    public FragmentDto getFragmentOfScholarInterDto(ScholarInterDto lastUsed) {
        return webClient.build()
                .get()
                .uri( "/scholarInter/fragment/" + lastUsed.getXmlId())
                .retrieve()
                .bodyToMono(FragmentDto.class)
                .blockOptional().get();
    }

    public FragmentDto getFragmentByXmlId(String xmlId) {
        return webClient.build()
                .get()
                .uri("/fragment/xmlId/" + xmlId)
                .retrieve()
                .bodyToMono(FragmentDto.class)
                .blockOptional().orElse(null);
    }
    
    


    public List<ScholarInterDto> getExpertEditionScholarInterDtoList(String acronymOfUsed) {
        return webClient.build()
                .get()
                .uri("/expertEdition/" + acronymOfUsed + "/scholarInterList")
                .retrieve()
                .bodyToFlux(ScholarInterDto.class)
                .collectList()
                .block();
    }

    public List<ScholarInterDto>  getScholarInterDtoListTwitterEdition(LocalDateTime editionBeginDateTime) {
        return webClient.build()
                .get()
                .uri( uriBuilder -> uriBuilder
                        .path("/scholarInter/fragment")
                        .queryParam("beginTime", editionBeginDateTime)
                        .build())
                .retrieve()
                .bodyToFlux(ScholarInterDto.class)
                .collectList()
                .block();
    }

    public void addDocumentToIndexer(String xmlId) throws IOException {
        webClient.build()
                .get()
                .uri( uriBuilder -> uriBuilder
                        .path("/addDocument")
                        .queryParam("xmlId", xmlId)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new IOException()))
                .bodyToMono(Void.class)
                .block();
    }


    public Set<InfoRangeDto> getInfoRangeDtoSetFromCitation(long id) {
        return webClient.build()
                .get()
                .uri("/citation/" + id + "/infoRange")
                .retrieve()
                .bodyToFlux(InfoRangeDto.class)
                .toStream()
                .collect(Collectors.toSet());
    }

    public boolean acronymExists(String acronym) {
        return webClient.build()
                .get()
                .uri("/expertEdition/" + acronym + "/exists")
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().get();
    }

    public String getScholarInterTitle(String usesScholarInterId) {
//        return webClient.get()
//                .uri("/scholarInter/" + usesScholarInterId + "/title")
//                .retrieve()
//                .bodyToMono(String.class)
//                .blockOptional().get();
        ScholarInterDto scholarInterDto = getScholarInterByXmlIdCache(usesScholarInterId).orElse(null);
        return scholarInterDto != null ? scholarInterDto.getTitle() : null;
    }


    public String getWriteFromPlainTextFragmentWriter(String xmlId) {
        return webClient.build()
                .get()
                .uri( uriBuilder -> uriBuilder
                        .path("/writeFromPlainTextFragmentWriter")
                        .queryParam("xmlId", xmlId)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    private Optional<ScholarInterDto> getScholarInterByXmlIdCache(String xmlId) {
        if (xmlId == null) {
            return Optional.empty();
        }
        ScholarInterDto scholarInterId = scholarInterMap.get(xmlId);
        if (scholarInterId == null) {
            scholarInterId = webClient.build()
                    .get()
                    .uri("/scholarInter/" + xmlId)
                    .retrieve()
                    .bodyToMono(ScholarInterDto.class)
                    .blockOptional().orElse(null);

            if (scholarInterId != null) {
                scholarInterMap.put(xmlId, scholarInterId);
            }
        }

        return Optional.ofNullable(scholarInterId);
    }

    public String getRepresentativeSourceInterExternalId(String fragmentXmlId) {
        return webClient.build()
                .get()
                .uri("/representativeSourceInter/" + fragmentXmlId + "/externalId")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().get();
    }


    public void createInfoRange(long id, String xmlId, String s, int htmlStart, String s1, int htmlEnd, String infoQuote, String infoText) {
         webClient.build()
                .post()
                .uri( uriBuilder -> uriBuilder
                        .path("/createInfoRange")
                        .queryParam("id", id)
                        .queryParam("xmlId", xmlId)
                        .queryParam("s", s)
                        .queryParam("htmlStart", htmlStart)
                        .queryParam("s1", s1)
                        .queryParam("htmlEnd", htmlEnd)
                        .queryParam("infoQuote", infoQuote)
                        .queryParam("infoText", infoText)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void createCitation(String fragXmlId, String sourceLink, String date, String fragText, long id) {
        webClient.build()
                .post()
                .uri( uriBuilder -> uriBuilder
                        .path("/createCitation")
                        .queryParam("fragXmlId", fragXmlId)
                        .queryParam("sourceLink", sourceLink)
                        .queryParam("date", date)
                        .queryParam("fragText", fragText)
                        .queryParam("id", id)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public String getWriteFromPlainHtmlWriter4OneInter(String xmlId, boolean highlightDiff) {
        return webClient.build()
                .get()
                .uri( uriBuilder -> uriBuilder
                        .path("/writeFromPlainHtmlWriter4OneInter/")
                        .queryParam("xmlId", xmlId)
                        .queryParam("highlightDiff", highlightDiff)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //      return this.textProvidesInterface.getWriteFromPlainHtmlWriter4OneInter(xmlId, highlightDiff);
    }

    public Set<FragmentDto> getFragmentDtoSet() {
        return webClient.build()
                .get()
                .uri("/fragments")
                .retrieve()
                .bodyToFlux(FragmentDto.class)
                .toStream()
                .collect(Collectors.toSet());
    }

    public SimpleTextDto getSimpleTextFromExternalId(String startTextId) {
        return webClient.build()
                .get()
                .uri("/simpleText/" + startTextId)
                .retrieve()
                .bodyToMono(SimpleTextDto.class)
                .blockOptional().get();
    }

    // Uses User Service

    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl(USER_SERVICE_URL);


    public UserDto getUser(String username) {
        return webClientUser.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user")
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .bodyToMono(UserDto.class)
                .blockOptional()
                .orElse(null);
        //        return this.userProvidesInterface.getUser(username);
    }

    public String exportXMLUsers() {
        return webClientUser.build()
                .get()
                .uri("/exportXMLUsers")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional()
                .orElse("");
    }
}
