package pt.ist.socialsoftware.edition.ldod.security;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.UserConnection_Base;

import java.util.Collections;
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
        List<String> localUserIds = LdoD.getInstance().getUserConnectionSet()
                .stream()
                .filter(uc -> uc.getProviderId().equals(key.getProviderId())
                        && uc.getProviderUserId().equals(key.getProviderUserId()))
                .map(UserConnection_Base::getUserId).collect(Collectors.toList());


        if (localUserIds.size() == 0 && connectionSignUp != null) {
            String newUserId = connectionSignUp.execute(connection);
            if (newUserId != null) {
                createConnectionRepository(newUserId).addConnection(connection);
                return Collections.singletonList(newUserId);
            }
        }
        return localUserIds;
    }

    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        return LdoD.getInstance().getUserConnectionSet().stream()
                .filter(uc -> uc.getProviderId().equals(providerId) && providerUserIds.contains(uc.getProviderUserId()))
                .map(UserConnection_Base::getUserId).collect(Collectors.toSet());
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        return new LdoDConnectionRepository(userId, connectionFactoryLocator, textEncryptor);
    }

}
