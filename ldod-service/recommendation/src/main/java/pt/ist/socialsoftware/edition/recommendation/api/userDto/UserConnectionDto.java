package pt.ist.socialsoftware.edition.recommendation.api.userDto;

import org.springframework.web.reactive.function.client.WebClient;


public class UserConnectionDto {

    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://localhost:8082/api");
//    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://docker-user:8082/api");


    private String userId;
    private String providerId;
    private String providerUserId;
    private String displayName;
    private String profileUrl;
    private String imageUrl;
    private String accessToken;
    private String secret;
    private String refreshToken;
    private long expireTime;
    private int rank;

    public UserConnectionDto() {

    }

    public String getUserId() {
        return userId;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getSecret() {
        return secret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public int getRank() {
        return rank;
    }

    public void updateUserConnection(String displayName, String profileUrl, String imageUrl, String accessToken, String secret, String refreshToken, Long expireTime) {
        webClientUser.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/updateUserConnection")
                    .queryParam("displayName", displayName)
                    .queryParam("profileUrl", profileUrl)
                    .queryParam("imageUrl", imageUrl)
                    .queryParam("accessToken", accessToken)
                    .queryParam("secret", secret)
                    .queryParam("refreshToken", refreshToken)
                    .queryParam("expireTime", expireTime)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.userProvidesInterface.updateUserConnection(this.userId, this.providerId, this.providerUserId, displayName, profileUrl, imageUrl, accessToken, secret, refreshToken, expireTime);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
