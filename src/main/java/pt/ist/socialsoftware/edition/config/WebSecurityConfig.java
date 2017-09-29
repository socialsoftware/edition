package pt.ist.socialsoftware.edition.config;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import pt.ist.socialsoftware.edition.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.security.LdoDAuthenticationSuccessHandler;
import pt.ist.socialsoftware.edition.security.LdoDSocialUserDetailsService;
import pt.ist.socialsoftware.edition.security.LdoDUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

	@Inject
	Environment environment;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.debug("configure");

		// to make accessible for unregistered users comment
		// .anyRequest().authenticated() after .antMatchers("/", "/auth/**",
		// "/signin/**", "/signup/**").permitAll()

		http.csrf().disable().formLogin().loginPage("/signin").successHandler(ldoDAuthenticationSuccessHandler())
				.loginProcessingUrl("/signin/authenticate").failureUrl("/signin?param.error=bad_credentials").and()
				.logout().logoutUrl("/signout").deleteCookies("JSESSIONID").invalidateHttpSession(true);

		http.authorizeRequests()
				// .antMatchers("/", "/error", "/webjars/**", "/auth/**", "/signin/**",
				// "/signup/**", "/about/**",
				// "/reading/**", "/source/**", "/edition/**", "/fragments/**", "/facs/**",
				// "/search/**",
				// "/encoding/**")
				// .permitAll()// .anyRequest().authenticated()
				.antMatchers("/virtualeditions/restricted/**").authenticated().antMatchers("/admin/**")
				.hasAuthority(RoleType.ROLE_ADMIN.name()).antMatchers("/user/**")
				.hasAuthority(RoleType.ROLE_ADMIN.name());

		http.sessionManagement().maximumSessions(2).sessionRegistry(sessionRegistry());
	}

	@Inject
	public void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		log.debug("registerAuthentication");

		auth.userDetailsService(ldoDUserDetailsService()).passwordEncoder(passwordEncoder());
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Bean
	public TextEncryptor textEncryptor() {
		return Encryptors.noOpText();
		// return
		// Encryptors.text(environment.getProperty("spring.encryption.password"),
		// KeyGenerators.string().generateKey());
	}

	@Bean
	public LdoDUserDetailsService ldoDUserDetailsService() {
		return new LdoDUserDetailsService();
	}

	@Bean
	public LdoDSocialUserDetailsService ldoDSocialUserDetailsService() {
		return new LdoDSocialUserDetailsService(ldoDUserDetailsService());
	}

	@Bean
	public LdoDAuthenticationSuccessHandler ldoDAuthenticationSuccessHandler() {
		return new LdoDAuthenticationSuccessHandler();
	}

}