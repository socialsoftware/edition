package pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto;


import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;
import java.util.stream.Collectors;

public class TwitterCitationDto  {

    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");

    private String tweetText;
    private String country;
    private String location;
    private String username;
    private String userProfileUrl;
    private long id;
    private long tweetId;

    public TwitterCitationDto() { }

    public String getTweetText() {
        return tweetText;
    }

    public String getCountry() {
        return country;
    }

    public String getLocation() {
        return location;
    }

    public String getUsername() {
        return username;
    }

    public long getId() {
        return id;
    }

    public long getTweetId() {
        return tweetId;
    }

    public boolean isTwitterCitation() {
        return true;
    }

    public String getUserProfileURL() {
        return this.userProfileUrl;
    }

    public Set<AwareAnnotationDto> getAwareAnnotationDtos() {
        return webClientVirtual.build()
                .get()
                .uri("/twitterCitation/" + tweetId + "/awareAnnotations")
                .retrieve()
                .bodyToFlux(AwareAnnotationDto.class)
                .toStream()
                .collect(Collectors.toSet());
    }


    public int getNumberOfRetweets() {
        return webClientVirtual.build()
                .get()
                .uri("/twitterCitation/" + tweetId + "/numberOfRetweets")
                .retrieve()
                .bodyToMono(Integer.class)
                .blockOptional().orElse(0);
    }

    public Set<TweetDto> getTweets() {
        return webClientVirtual
                .build()
                .get()
                .uri("/twitterCitation/" + tweetId + "/tweets")
                .retrieve()
                .bodyToFlux(TweetDto.class)
                .toStream().collect(Collectors.toSet());
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }
}
