package pt.ist.socialsoftware.edition.ldod.domain;

import org.springframework.dao.DuplicateKeyException;

public class UserConnection extends UserConnection_Base {

    public UserConnection(LdoD ldod, String userId, String providerId, String providerUserId, int rank,
                          String displayName, String profileUrl, String imageUrl, String accessToken, String secret,
                          String refreshToken, Long expireTime) {
        checkUnique(ldod, userId, providerId, rank);

        setLdoD(ldod);
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

    private void checkUnique(LdoD ldod, String userId, String providerId, int rank) {
        if (ldod.getUserConnectionSet().stream().anyMatch(
                uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(providerId) && (uc.getRank() == rank))) {
            throw new DuplicateKeyException(providerId);
        }

    }

    public void remove() {
        setLdoD(null);
        deleteDomainObject();
    }

}
