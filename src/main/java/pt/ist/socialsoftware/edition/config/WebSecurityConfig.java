package pt.ist.socialsoftware.edition.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).withDefaultSchema();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/resources/**")
                .permitAll().anyRequest().authenticated()
                .antMatchers("/virtualeditions/restricted/**").authenticated()
                .antMatchers("/admin/**").hasAuthority("ADMIN").and()
                .formLogin().failureUrl("/login?errorLogin")
                .defaultSuccessUrl("/").loginPage("/login").permitAll().and()
                .apply(new SpringSocialConfigurer().postLoginUrl("/")
                        .alwaysUsePostLoginUrl(true))
                .and().logout().logoutUrl("/logout")
                .deleteCookies("remember-me").logoutSuccessUrl("/").permitAll()
                .and().rememberMe();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }

}