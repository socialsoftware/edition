package pt.ist.socialsoftware.edition.user.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.ist.socialsoftware.edition.user.utils.Emailer;

import javax.jms.Queue;

@PropertySource({"classpath:application.properties", "classpath:specific.properties", "classpath:secrete.properties"})
@ComponentScan(basePackages = "pt.ist.socialsoftware.edition.user")
@SpringBootApplication
@Configuration
@EnableJms
public class UserApplication extends SpringBootServletInitializer implements InitializingBean {


    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
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

    @Bean
    public Emailer emailer() {
        return new Emailer();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
}
