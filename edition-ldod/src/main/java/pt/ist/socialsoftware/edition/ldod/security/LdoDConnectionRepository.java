package pt.ist.socialsoftware.edition.ldod.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.UserConnection;

public class LdoDConnectionRepository implements ConnectionRepository {

	private final String userId;

	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final TextEncryptor textEncryptor;

	public LdoDConnectionRepository(String userId, ConnectionFactoryLocator connectionFactoryLocator,
			TextEncryptor textEncryptor) {
		this.userId = userId;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
	}

	@Override
	public MultiValueMap<String, Connection<?>> findAllConnections() {
		List<Connection<?>> connections = LdoD.getInstance().getUserConnectionSet().stream()
				.filter(uc -> uc.getUserId().equals(userId)).sorted((uc1, uc2) -> compareByProviderIdAndRank(uc1, uc2))
				.map(uc -> mapUserConnection(uc)).collect(Collectors.toList());

		MultiValueMap<String, Connection<?>> connectionsMap = new LinkedMultiValueMap<String, Connection<?>>();
		Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
		for (String registeredProviderId : registeredProviderIds) {
			connectionsMap.put(registeredProviderId, Collections.<Connection<?>> emptyList());
		}
		for (Connection<?> connection : connections) {
			String providerId = connection.getKey().getProviderId();
			if (connectionsMap.get(providerId).size() == 0) {
				connectionsMap.put(providerId, new LinkedList<Connection<?>>());
			}
			connectionsMap.add(providerId, connection);
		}
		return connectionsMap;
	}

	@Override
	public List<Connection<?>> findConnections(String providerId) {
		return LdoD.getInstance().getUserConnectionSet().stream()
				.filter(uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(providerId))
				.sorted((uc1, uc2) -> Integer.compare(uc1.getRank(), uc2.getRank())).map(uc -> mapUserConnection(uc))
				.collect(Collectors.toList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> List<Connection<A>> findConnections(Class<A> apiType) {
		List<?> connections = findConnections(getProviderId(apiType));
		return (List<Connection<A>>) connections;
	}

	@Override
	public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUsers) {
		if (providerUsers == null || providerUsers.isEmpty()) {
			throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
		}

		List<Connection<?>> allConnections = providerUsers.entrySet().stream()
				.flatMap(entry -> entry.getValue().stream()
						.map(u -> LdoD.getInstance().getUserConnection(userId, entry.getKey(), u)))
				.sorted((uc1, uc2) -> compareByProviderIdAndRank(uc1, uc2)).map(uc -> mapUserConnection(uc))
				.collect(Collectors.toList());

		MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
		for (Connection<?> connection : allConnections) {
			String providerId = connection.getKey().getProviderId();
			List<String> userIds = providerUsers.get(providerId);
			List<Connection<?>> connections = connectionsForUsers.get(providerId);
			if (connections == null) {
				connections = new ArrayList<Connection<?>>(userIds.size());
				for (int i = 0; i < userIds.size(); i++) {
					connections.add(null);
				}
				connectionsForUsers.put(providerId, connections);
			}
			String providerUserId = connection.getKey().getProviderUserId();
			int connectionIndex = userIds.indexOf(providerUserId);
			connections.set(connectionIndex, connection);
		}
		return connectionsForUsers;
	}

	@Override
	public Connection<?> getConnection(ConnectionKey connectionKey) {
		UserConnection userConnection = LdoD.getInstance().getUserConnectionSet().stream()
				.filter(uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(connectionKey.getProviderId())
						&& uc.getProviderUserId().equals(connectionKey.getProviderUserId()))
				.findFirst().orElseThrow(() -> new NoSuchConnectionException(connectionKey));

		return mapUserConnection(userConnection);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
		String providerId = getProviderId(apiType);
		return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
		String providerId = getProviderId(apiType);
		Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
		if (connection == null) {
			throw new NotConnectedException(providerId);
		}
		return connection;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
		String providerId = getProviderId(apiType);
		return (Connection<A>) findPrimaryConnection(providerId);
	}

	@Override
	@Atomic(mode = TxMode.WRITE)
	public void addConnection(Connection<?> connection) {
		try {
			ConnectionData data = connection.createData();

			int nextRank = LdoD.getInstance().getUserConnectionSet().stream()
					.filter(uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(data.getProviderId()))
					.max((uc1, uc2) -> Integer.compare(uc1.getRank(), uc2.getRank())).map(uc -> uc.getRank() + 1)
					.orElse(1);

			LdoD.getInstance().createUserConnection(userId, data.getProviderId(), data.getProviderUserId(), nextRank,
					data.getDisplayName(), data.getProfileUrl(), data.getImageUrl(), encrypt(data.getAccessToken()),
					encrypt(data.getSecret()), encrypt(data.getRefreshToken()), data.getExpireTime());

		} catch (DuplicateKeyException e) {
			throw new DuplicateConnectionException(connection.getKey());
		}
	}

	@Override
	@Atomic(mode = TxMode.WRITE)
	public void updateConnection(Connection<?> connection) {
		ConnectionData data = connection.createData();

		LdoD.getInstance().getUserConnectionSet().stream()
				.filter(uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(data.getProviderId())
						&& uc.getProviderUserId().equals(data.getProviderUserId()))
				.forEach(uc -> {
					uc.setDisplayName(data.getDisplayName());
					uc.setProfileUrl(data.getProfileUrl());
					uc.setImageUrl(data.getImageUrl());
					uc.setAccessToken(encrypt(data.getAccessToken()));
					uc.setSecret(encrypt(data.getSecret()));
					uc.setRefreshToken(encrypt(data.getRefreshToken()));
					uc.setExpireTime(data.getExpireTime());
				});
	}

	@Override
	@Atomic(mode = TxMode.WRITE)
	public void removeConnections(String providerId) {
		Set<UserConnection> userConnections = LdoD.getInstance().getUserConnectionSet();
		Set<UserConnection> toRemoveUserConnections = userConnections.stream()
				.filter(uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(providerId))
				.collect(Collectors.toSet());

		userConnections.removeAll(toRemoveUserConnections);
	}

	@Override
	@Atomic(mode = TxMode.WRITE)
	public void removeConnection(ConnectionKey connectionKey) {
		Set<UserConnection> userConnections = LdoD.getInstance().getUserConnectionSet();
		Set<UserConnection> toRemoveUserConnections = userConnections.stream()
				.filter(uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(connectionKey.getProviderId())
						&& uc.getProviderUserId().equals(connectionKey.getProviderUserId()))
				.collect(Collectors.toSet());

		userConnections.removeAll(toRemoveUserConnections);
	}

	// internal helpers

	private Connection<?> findPrimaryConnection(String providerId) {
		Optional<UserConnection> optUserConnection = LdoD.getInstance().getUserConnectionSet().stream()
				.filter(uc -> uc.getUserId().equals(userId) && uc.getProviderId().equals(providerId))
				.sorted((uc1, uc2) -> Integer.compare(uc1.getRank(), uc2.getRank())).findFirst();

		if (!optUserConnection.isPresent())
			return null;
		else
			return mapUserConnection(optUserConnection.get());
	}

	private Connection<?> mapUserConnection(UserConnection userConnection) {
		ConnectionData connectionData = mapConnectionData(userConnection);
		ConnectionFactory<?> connectionFactory = connectionFactoryLocator
				.getConnectionFactory(connectionData.getProviderId());
		return connectionFactory.createConnection(connectionData);
	}

	private ConnectionData mapConnectionData(UserConnection userConnection) {
		return new ConnectionData(userConnection.getProviderId(), userConnection.getProviderUserId(),
				userConnection.getDisplayName(), userConnection.getProfileUrl(), userConnection.getImageUrl(),
				decrypt(userConnection.getAccessToken()), decrypt(userConnection.getSecret()),
				decrypt(userConnection.getRefreshToken()), expireTime(userConnection.getExpireTime()));
	}

	private String decrypt(String encryptedText) {
		return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
	}

	private Long expireTime(long expireTime) {
		return expireTime == 0 ? null : expireTime;
	}

	private <A> String getProviderId(Class<A> apiType) {
		return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
	}

	private String encrypt(String text) {
		return text != null ? textEncryptor.encrypt(text) : text;
	}

	private int compareByProviderIdAndRank(UserConnection uc1, UserConnection uc2) {
		if (uc1.getProviderId().compareTo(uc2.getProviderId()) != 0)
			return uc1.getProviderId().compareTo(uc2.getProviderId());

		return Integer.compare(uc1.getRank(), uc2.getRank());
	}

}
