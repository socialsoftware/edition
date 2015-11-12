package pt.ist.socialsoftware.edition.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import pt.ist.socialsoftware.edition.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.security.LdoDAuthenticationSuccessHandler;
import pt.ist.socialsoftware.edition.security.LdoDSocialUserDetailsService;
import pt.ist.socialsoftware.edition.security.LdoDUserDetailsService;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static Logger log = LoggerFactory
            .getLogger(WebSecurityConfig.class);

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.debug("configure");

        http.csrf().disable().formLogin().loginPage("/signin")
                .successHandler(ldoDAuthenticationSuccessHandler())
                .loginProcessingUrl("/signin/authenticate")
                .failureUrl("/signin?param.error=bad_credentials").and()
                .logout().logoutUrl("/signout").deleteCookies("JSESSIONID")
                .and().authorizeRequests()
                .antMatchers("/", "/auth/**", "/signin/**", "/signup/**")
                .permitAll().anyRequest().authenticated()
                .antMatchers("/virtualeditions/restricted/**").authenticated()
                .antMatchers("/admin/**")
                .hasAuthority(RoleType.ROLE_ADMIN.name()).and().rememberMe();
    }

    @Autowired
    public void registerAuthentication(AuthenticationManagerBuilder auth)
            throws Exception {
        log.debug("registerAuthentication");

        auth.userDetailsService(ldoDUserDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
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