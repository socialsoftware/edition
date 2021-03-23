package pt.ist.socialsoftware.edition.user.domain;

import org.springframework.dao.DuplicateKeyException;


public class UserConnection extends UserConnection_Base {

    public UserConnection(UserModule userModule, String userId, String providerId, String providerUserId, int rank,
                          String displayName, String profileUrl, String imageUrl, String accessToken, String secret,
                          String refreshToken, Long expireTime) {
        checkUnique(userModule, userId, providerId, rank);

        setUserModule(userModule);
        setUserId(userId);
        setProviderId(providerId);
        setProviderUserId(providerUserId);
        setRank(rank);
        setDisplayName(displayName);
        setProfileUrl(profileUrl);
        setImageUrl(imageUrl);
        setAccessToken(accessToken);
        setSecret(secret);
        setRefreshToken(refreshToken);
        setExpireTime(expireTime);
    }

    private void checkUnique(UserModule userModule, String userId, String providerId, int rank) {
        if (userModule.getUserConnectionSet().stream().filter(
                uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(providerId) && (uc.getRank() == rank))
                .findFirst().isPresent()) {
            throw new DuplicateKeyException(providerId);
        }
    }

    public void remove() {
        setUserModule(null);

        deleteDomainObject();
    }

}
