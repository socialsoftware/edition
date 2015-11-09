package pt.ist.socialsoftware.edition.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

    @Inject
    private DataSource dataSource;

    @Override
    public void addConnectionFactories(
            ConnectionFactoryConfigurer connectionFactoryConfigurer,
            Environment environment) {
        connectionFactoryConfigurer
                .addConnectionFactory(new FacebookConnectionFactory(
                        environment.getProperty("spring.social.facebook.appId"),
                        environment.getProperty(
                                "spring.social.facebook.appSecret")));
        connectionFactoryConfigurer
                .addConnectionFactory(new TwitterConnectionFactory(
                        environment.getProperty("spring.social.twitter.appId"),
                        environment.getProperty(
                                "spring.social.twitter.appSecret")));
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new UserIdSource() {
            @Override
            public String getUserId() {
                Authentication authentication = SecurityContextHolder
                        .getContext().getAuthentication();
                if (authentication == null) {
                    throw new IllegalStateException(
                            "Unable to get a ConnectionRepository: no user signed in");
                }
                return authentication.getName();
            }
        };
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(
            ConnectionFactoryLocator connectionFactoryLocator) {
        return new JdbcUsersConnectionRepository(dataSource,
                connectionFactoryLocator, Encryptors.noOpText());
    }

    //
    // API Binding Beans
    //

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public Facebook facebook(ConnectionRepository repository) {
        Connection<Facebook> connection = repository
                .findPrimaryConnection(Facebook.class);
        return connection != null ? connection.getApi() : null;
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public Twitter twitter(ConnectionRepository repository) {
        Connection<Twitter> connection = repository
                .findPrimaryConnection(Twitter.class);
        return connection != null ? connection.getApi() : null;
    }

    //
    // Web Controller and Filter Beans
    //
    // @Bean
    // public ConnectController connectController(
    // ConnectionFactoryLocator connectionFactoryLocator,
    // ConnectionRepository connectionRepository) {
    // ConnectController connectController = new ConnectController(
    // connectionFactoryLocator, connectionRepository);
    // connectController
    // .addInterceptor(new PostToWallAfterConnectInterceptor());
    // connectController.addInterceptor(new TweetAfterConnectInterceptor());
    // return connectController;
    // }
    //
    // @Bean
    // public ProviderSignInController providerSignInController(
    // ConnectionFactoryLocator connectionFactoryLocator,
    // UsersConnectionRepository usersConnectionRepository) {
    // return new ProviderSignInController(connectionFactoryLocator,
    // usersConnectionRepository,
    // new SimpleSignInAdapter(new HttpSessionRequestCache()));
    // }
    //
    // @Bean
    // public DisconnectController disconnectController(
    // UsersConnectionRepository usersConnectionRepository,
    // Environment env) {
    // return new DisconnectController(usersConnectionRepository,
    // env.getProperty("facebook.appSecret"));
    // }
    //
    // @Bean
    // public ReconnectFilter apiExceptionHandler(
    // UsersConnectionRepository usersConnectionRepository,
    // UserIdSource userIdSource) {
    // return new ReconnectFilter(usersConnectionRepository, userIdSource);
    // }

}