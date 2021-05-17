package pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto;

import org.springframework.web.reactive.function.client.WebClient;

public class TweetDto {

//    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");
    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://docker-virtual:8083/api");


    private long id;

    public TweetDto() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
