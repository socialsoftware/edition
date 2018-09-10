package pt.ist.socialsoftware.edition.ldod.security;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;
import pt.ist.socialsoftware.edition.ldod.domain.UserManager;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LdoDUsersConnectionRepository implements UsersConnectionRepository {

	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final TextEncryptor textEncryptor;

	private ConnectionSignUp connectionSignUp;

	public LdoDUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator,
			TextEncryptor textEncryptor) {
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
	}

	public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
		this.connectionSignUp = connectionSignUp;
	}

	@Override
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		ConnectionKey key = connection.getKey();
		List<String> localUserIds = UserManager.getInstance().getUserConnectionSet().stream()
				.filter(uc -> uc.getProviderId().equals(key.getProviderId())
						&& uc.getProviderUserId().equals(key.getProviderUserId()))
				.map(uc -> uc.getUserId()).collect(Collectors.toList());

		if (localUserIds.size() == 0 && connectionSignUp != null) {
			String newUserId = connectionSignUp.execute(connection);
			if (newUserId != null) {
				createConnectionRepository(newUserId).addConnection(connection);
				return Arrays.asList(newUserId);
			}
		}
		return localUserIds;
	}

	@Override
	public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
		return UserManager.getInstance().getUserConnectionSet().stream()
				.filter(uc -> uc.getProviderId().equals(providerId) && providerUserIds.contains(uc.getProviderUserId()))
				.map(uc -> uc.getUserId()).collect(Collectors.toSet());
	}

	@Override
	public ConnectionRepository createConnectionRepository(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("userId cannot be null");
		}
		return new LdoDConnectionRepository(userId, connectionFactoryLocator, textEncryptor);
	}

}
