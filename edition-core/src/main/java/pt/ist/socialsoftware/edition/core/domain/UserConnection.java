package pt.ist.socialsoftware.edition.core.domain;

import org.springframework.dao.DuplicateKeyException;

import pt.ist.socialsoftware.edition.core.domain.UserConnection_Base;

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
		if (ldod.getUserConnectionSet().stream().filter(
				uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(providerId) && (uc.getRank() == rank))
				.findFirst().isPresent())
			throw new DuplicateKeyException(providerId);
	}

	public void remove() {
		setLdoD(null);

		deleteDomainObject();
	}

}
