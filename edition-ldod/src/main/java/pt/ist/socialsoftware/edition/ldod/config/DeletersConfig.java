package pt.ist.socialsoftware.edition.ldod.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pt.ist.socialsoftware.edition.ldod.deleters.ExpertEditionInterDeleterVirtual;
import pt.ist.socialsoftware.edition.ldod.deleters.FragmentDeleterVirtual;
import pt.ist.socialsoftware.edition.text.deleters.ExpertEditionInterDeleter;
import pt.ist.socialsoftware.edition.text.deleters.FragmentDeleter;

@Configuration
@ComponentScan(basePackages = "pt.ist.socialsoftware.edition")
public class DeletersConfig {
    @Bean
    public FragmentDeleter fragmentDeleter() {
        return new FragmentDeleterVirtual();
    }

    @Bean
    public ExpertEditionInterDeleter expertEditionInterDeleter() {
        return new ExpertEditionInterDeleterVirtual();
    }
}
