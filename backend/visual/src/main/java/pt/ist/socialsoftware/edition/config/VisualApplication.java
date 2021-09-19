package pt.ist.socialsoftware.edition.config;


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


@ComponentScan(basePackages = "pt.ist.socialsoftware.edition")
@SpringBootApplication
@Configuration
@EnableJms
public class VisualApplication extends SpringBootServletInitializer implements InitializingBean {


    public static void main(String[] args) {
        SpringApplication.run(VisualApplication.class, args);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new Jackson2ObjectMapperBuilder().serializationInclusion(JsonInclude.Include.NON_NULL).build();
    }

    @Bean
    public ActiveMQTopic queue(){
        return new ActiveMQTopic("test-topic");
    }

}
