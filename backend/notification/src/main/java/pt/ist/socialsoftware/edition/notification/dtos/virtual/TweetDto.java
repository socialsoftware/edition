package pt.ist.socialsoftware.edition.notification.dtos.virtual;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.reactive.function.client.WebClient;

import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.VIRTUAL_SERVICE_URL;

public class TweetDto {

    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl(VIRTUAL_SERVICE_URL);


    private long id;

    public TweetDto() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonIgnore
    public void remove() {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/removeTweet")
                    .queryParam("id", id)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
