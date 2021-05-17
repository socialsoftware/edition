package pt.ist.socialsoftware.edition.recommendation.api.virtualDto;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class HumanAnnotationDto extends AnnotationDto {

//    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");
    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://docker-virtual:8083/api");


    private String text;
    private List<TagDto> tags;

    public HumanAnnotationDto() {
        super();
    }

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
