package pt.ist.socialsoftware.edition.user.domain;

import org.springframework.dao.DuplicateKeyException;

public class UserConnection extends UserConnection_Base {

	public UserConnection(UserManager userManager, String userId, String providerId, String providerUserId, int rank,
                          String displayName, String profileUrl, String imageUrl, String accessToken, String secret,
                          String refreshToken, Long expireTime) {
		checkUnique(userManager, userId, providerId, rank);

		setUserManager(userManager);
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

	private void checkUnique(UserManager userManager, String userId, String providerId, int rank) {
		if (userManager.getUserConnectionSet().stream().filter(
				uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(providerId) && (uc.getRank() == rank))
				.findFirst().isPresent())
			throw new DuplicateKeyException(providerId);
	}

	public void remove() {
		setUserManager(null);

		deleteDomainObject();
	}

}
