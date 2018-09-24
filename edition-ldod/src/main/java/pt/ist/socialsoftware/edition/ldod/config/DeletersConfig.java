package pt.ist.socialsoftware.edition.ldod.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pt.ist.socialsoftware.edition.ldod.deleters.ExpertEditionInterDeleterVirtual;
import pt.ist.socialsoftware.edition.ldod.deleters.FragInterDeleterVirtual;
import pt.ist.socialsoftware.edition.ldod.deleters.FragmentDeleterVirtual;
import pt.ist.socialsoftware.edition.ldod.deleters.SourceInterDeleterVirtual;
import pt.ist.socialsoftware.edition.text.deleters.ExpertEditionInterDeleter;
import pt.ist.socialsoftware.edition.text.deleters.FragInterDeleter;
import pt.ist.socialsoftware.edition.text.deleters.FragmentDeleter;
import pt.ist.socialsoftware.edition.text.deleters.SourceInterDeleter;

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

    @Bean
    public SourceInterDeleter sourceInterDeleter() {
        return new SourceInterDeleterVirtual();
    }

    @Bean
    public FragInterDeleter fragInterDeleter() {
        return new FragInterDeleterVirtual();
    }
}
