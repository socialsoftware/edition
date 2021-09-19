package pt.ist.socialsoftware.edition.notification.dtos.virtual;



import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.VIRTUAL_SERVICE_URL;

public class HumanAnnotationDto extends AnnotationDto {

    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl(VIRTUAL_SERVICE_URL);


    private String text;
    private List<TagDto> tags;

    public HumanAnnotationDto() {
        super();
    }

    @JsonIgnore
    public static boolean canCreate(String acronym, String username) {
        WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + acronym + "/canCreateHumanAnnotation")
                    .queryParam("username", username)
                    .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional()
                .orElse(false);
        //        return virtualProvidesInterface.canCreateHumanAnnotationOnVirtualEdition(acronym, username);
    }

    @JsonIgnore
    public boolean canUpdate(String id, String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/humanAnnotation/" + id + "/canUserUpdate")
                    .queryParam("user", username)
                    .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //        return this.virtualProvidesInterface.canUserUpdateHumanAnnotation(id, username);
    }

    @JsonIgnore
    public void update(String text, List<String> tags) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/humanAnnotation/" + getExternalId() + "/update")
                    .queryParam("text", text)
                    .queryParam("tags", tags)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.updateHumanAnnotation(getExternalId(), text, tags);
    }

    @JsonIgnore
    public boolean canDelete(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/humanAnnotation/" + getExternalId() + "/canUserDelete")
                    .queryParam("user", username)
                    .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //       return this.virtualProvidesInterface.canUserDeleteHumanAnnotation(getExternalId(), username);
    }

    @JsonIgnore
    public void remove() {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/removeHumanAnnotation")
                    .queryParam("id", getExternalId())
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.removeHumanAnnotation(getExternalId());
    }

    @JsonIgnore
    public VirtualEditionInterDto getVirtualEditionInter() {
        return webClientVirtual.build()
                .get()
                .uri("/humanAnnotation/" + getExternalId() + "/virtualEditionInter")
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TagDto> getTags() {
        return this.tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    @Override
    public boolean isHumanAnnotation() {
        return true;
    }
}
