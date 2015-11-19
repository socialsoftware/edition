package pt.ist.socialsoftware.edition.security;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;

import pt.ist.socialsoftware.edition.domain.LdoD;

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
		List<String> localUserIds = LdoD.getInstance().getUserConnectionSet().stream()
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
		return LdoD.getInstance().getUserConnectionSet().stream()
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
