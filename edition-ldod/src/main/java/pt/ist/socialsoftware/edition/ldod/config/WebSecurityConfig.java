package pt.ist.socialsoftware.edition.ldod.config;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import pt.ist.socialsoftware.edition.ldod.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.ldod.filters.JWTAuthorizationFilter;
import pt.ist.socialsoftware.edition.ldod.security.LdoDAuthenticationSuccessHandler;
import pt.ist.socialsoftware.edition.ldod.security.LdoDSocialUserDetailsService;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetailsService;
import pt.ist.socialsoftware.edition.ldod.security.jwt.JWTAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

	@Inject
	Environment environment;

	@Autowired
	private JWTAuthenticationEntryPoint unauthorizedHandler;

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
				.logout().logoutUrl("/signout").deleteCookies("JSESSIONID").invalidateHttpSession(true).and()
				.authorizeRequests().antMatchers("/virtualeditions/restricted/**", "/user/**", "/api/microfrontend/virtual/restricted/**").authenticated()
				.antMatchers("/admin/**").hasAuthority(RoleType.ROLE_ADMIN.name())
				.and().sessionManagement()
				.maximumSessions(2).sessionRegistry(sessionRegistry());

		http.cors().and().authorizeRequests().antMatchers("/api/user/**", "/api/services/**").authenticated().and()
				.exceptionHandling().authenticationEntryPoint(this.unauthorizedHandler).and()
				.addFilter(new JWTAuthorizationFilter(authenticationManager()));// .sessionManagement()
		// .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public LdoDAuthenticationSuccessHandler ldoDAuthenticationSuccessHandler() {
		return new LdoDAuthenticationSuccessHandler();
	}

	@Inject
	public void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		log.debug("registerAuthentication");

		auth.userDetailsService(ldoDUserDetailsService()).passwordEncoder(passwordEncoder());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
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

}