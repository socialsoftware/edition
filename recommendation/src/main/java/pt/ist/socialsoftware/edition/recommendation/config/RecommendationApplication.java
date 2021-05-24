package pt.ist.socialsoftware.edition.recommendation.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jms.annotation.EnableJms;


@PropertySource({"classpath:application.properties", "classpath:specific.properties", "classpath:secrete.properties"})
@ComponentScan(basePackages = "pt.ist.socialsoftware.edition.recommendation")
@SpringBootApplication
@Configuration
@EnableJms
public class RecommendationApplication extends SpringBootServletInitializer implements InitializingBean {

    public static void main(String[] args) {
        SpringApplication.run(RecommendationApplication.class, args);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new Jackson2ObjectMapperBuilder().serializationInclusion(JsonInclude.Include.NON_NULL).build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Bean
    public ActiveMQTopic queue(){
        return new ActiveMQTopic("test-topic");
    }
}
