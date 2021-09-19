package pt.ist.socialsoftware.edition.notification.dtos.virtual;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;
import java.util.stream.Collectors;

import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.VIRTUAL_SERVICE_URL;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({@JsonSubTypes.Type(value = AwareAnnotationDto.class, name = AnnotationDto.AWARE),
        @JsonSubTypes.Type(value = HumanAnnotationDto.class, name = AnnotationDto.HUMAN)})
public abstract class AnnotationDto {

    public static final String AWARE = "aware";
    public static final String HUMAN = "human";

    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl(VIRTUAL_SERVICE_URL);


    private String quote;
    private String username;
    private String text;
    private String externalId;
    private String interExternalId;
    private String interXmlId;
    private String user;

    public AnnotationDto() {
    }

    public String getQuote() {
        return this.quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getInterExternalId() {
        return interExternalId;
    }

//    public Set<Range> getRangeSet() {
//        return this.virtualProvidesInterface.getRangeSetFromAnnotation(externalId);
//    }

    @JsonIgnore
    public Set<RangeJson> getRangeSet() {
        return webClientVirtual.build()
                .get()
                .uri("/annotation/" + externalId + "/ranges")
                .retrieve()
                .bodyToFlux(RangeJson.class)
                .toStream()
                .collect(Collectors.toSet());
    }

    public String getUser() {
        return user;
    }

    @JsonIgnore
    public VirtualEditionDto getVirtualEdition() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/" + interXmlId + "/virtualEdition")
                .retrieve()
                .bodyToMono(VirtualEditionDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionOfVirtualEditionInter(interXmlId);
    }

    public abstract boolean isHumanAnnotation();

    @JsonIgnore
    public void remove() {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/removeAnnotation")
                    .queryParam("externalId", externalId)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setInterExternalId(String interExternalId) {
        this.interExternalId = interExternalId;
    }

    public void setInterXmlId(String interXmlId) {
        this.interXmlId = interXmlId;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
