package pt.ist.socialsoftware.edition.ldod.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomScopeConfig {

	@Bean
	public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
		return new CustomScopeRegisteringBeanFactoryPostProcessor();
	}
}
