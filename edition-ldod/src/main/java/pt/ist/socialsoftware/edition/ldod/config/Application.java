package pt.ist.socialsoftware.edition.ldod.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import pt.ist.socialsoftware.edition.ldod.security.LdoDSignInAdapter;
import pt.ist.socialsoftware.edition.ldod.utils.Bootstrap;
import pt.ist.socialsoftware.edition.ldod.utils.Emailer;

@PropertySource({ "classpath:application.properties", "classpath:specific.properties", "classpath:secrete.properties" })
@ComponentScan(basePackages = "pt.ist.socialsoftware.edition")
@SpringBootApplication
@EnableScheduling
public class Application extends SpringBootServletInitializer implements InitializingBean {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void afterPropertiesSet() {
		Bootstrap.initializeSystem();
	}

	@Bean
	public SignInAdapter signInAdapter() {
		return new LdoDSignInAdapter(new HttpSessionRequestCache());
	}

	@Bean(name = "messageSource")
	public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
		CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter() {
			@Override
			protected void beforeRequest(HttpServletRequest request, String message) {
			}

			@Override
			protected void afterRequest(HttpServletRequest request, String message) {
				if (!message.contains("webjars") && !message.contains("favicon") && !message.contains("resources")) {
					this.logger.debug(message);
				}
			}
		};
		loggingFilter.setIncludeClientInfo(true);
		loggingFilter.setIncludeQueryString(true);
		loggingFilter.setIncludePayload(true);
		loggingFilter.setIncludeHeaders(false);
		loggingFilter.setAfterMessagePrefix("LOGGING: ");
		return loggingFilter;
	}

	@Bean
	public Emailer emailer() {
		return new Emailer();
	}


}
