package pt.ist.socialsoftware.edition.user.api.dto;

import pt.ist.socialsoftware.edition.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.user.domain.UserConnection;

public class UserConnectionDto {

    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

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

    public UserConnectionDto(UserConnection userConnection) {
        this.userId = userConnection.getUserId();
        this.providerId = userConnection.getProviderId();
        this.providerUserId = userConnection.getProviderUserId();
        this.displayName = userConnection.getDisplayName();
        this.profileUrl = userConnection.getProfileUrl();
        this.imageUrl = userConnection.getImageUrl();
        this.accessToken = userConnection.getAccessToken();
        this.secret = userConnection.getSecret();
        this.refreshToken = userConnection.getRefreshToken();
        this.expireTime = userConnection.getExpireTime();
        this.rank = userConnection.getRank();
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
        this.userProvidesInterface.updateUserConnection(this.userId, this.providerId, this.providerUserId, displayName, profileUrl, imageUrl, accessToken, secret, refreshToken, expireTime);
    }
}
