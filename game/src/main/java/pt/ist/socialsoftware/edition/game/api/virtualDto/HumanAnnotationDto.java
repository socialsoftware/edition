package pt.ist.socialsoftware.edition.game.api.virtualDto;


import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class HumanAnnotationDto extends AnnotationDto {

    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");
//    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://docker-virtual:8083/api");


    private String text;
    private List<TagDto> tags;

    public HumanAnnotationDto() {
        super();
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
