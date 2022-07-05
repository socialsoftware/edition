package pt.ist.socialsoftware.edition.ldod.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import pt.ist.socialsoftware.edition.ldod.security.LdoDSignInAdapter;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUsersConnectionRepository;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

	@Inject
	TextEncryptor textEncryptor;

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
		GoogleConnectionFactory gcf = new GoogleConnectionFactory(env.getProperty("spring.social.google.appId"),
				env.getProperty("spring.social.google.appSecret"));
		gcf.setScope("profile");
		cfConfig.addConnectionFactory(gcf);

		FacebookConnectionFactory fcf = new FacebookConnectionFactory(env.getProperty("spring.social.facebook.appId"),
				env.getProperty("spring.social.facebook.appSecret"));
		fcf.setScope("profile");
		cfConfig.addConnectionFactory(fcf);

		TwitterConnectionFactory tcf = new TwitterConnectionFactory(env.getProperty("spring.social.twitter.appId"),
				env.getProperty("spring.social.twitter.appSecret"));
		cfConfig.addConnectionFactory(tcf);

		LinkedInConnectionFactory lcf = new LinkedInConnectionFactory(env.getProperty("spring.social.linkedin.appId"),
				env.getProperty("spring.social.linkedin.appSecret"));
		cfConfig.addConnectionFactory(lcf);
	}

	@Override
	public UserIdSource getUserIdSource() {
		return new UserIdSource() {
			@Override
			public String getUserId() {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication == null) {
					throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
				}
				return authentication.getName();
			}
		};
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		// return new
		// InMemoryUsersConnectionRepository(connectionFactoryLocator);
		return new LdoDUsersConnectionRepository(connectionFactoryLocator, this.textEncryptor);
	}

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public Facebook facebook(ConnectionRepository repository) {
		Connection<Facebook> connection = repository.findPrimaryConnection(Facebook.class);
		return connection != null ? connection.getApi() : null;
	}

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public Twitter twitter(ConnectionRepository repository) {
		Connection<Twitter> connection = repository.findPrimaryConnection(Twitter.class);
		return connection != null ? connection.getApi() : null;
	}

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public LinkedIn linkedin(ConnectionRepository repository) {
		Connection<LinkedIn> connection = repository.findPrimaryConnection(LinkedIn.class);
		return connection != null ? connection.getApi() : null;
	}

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public Google google(ConnectionRepository repository) {
		Connection<Google> connection = repository.findPrimaryConnection(Google.class);
		return connection != null ? connection.getApi() : new GoogleTemplate();
	}

	@Bean
	public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator,
			ConnectionRepository connectionRepository) {
		ConnectController connectController = new ConnectController(connectionFactoryLocator, connectionRepository);
		return connectController;
	}

	@Bean
	public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator,
			UsersConnectionRepository usersConnectionRepository) {
		return new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository,
				new LdoDSignInAdapter(new HttpSessionRequestCache()));
	}

}
