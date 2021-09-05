package pt.ist.socialsoftware.edition.game.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.ist.socialsoftware.edition.game.config.CustomScopeRegisteringBeanFactoryPostProcessor;

@Configuration
public class CustomScopeConfig {

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new CustomScopeRegisteringBeanFactoryPostProcessor();
    }
}
