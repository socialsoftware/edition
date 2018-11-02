package pt.ist.socialsoftware.edition.ldod.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.ist.socialsoftware.edition.ldod.deleters.*;
import pt.ist.socialsoftware.edition.text.deleters.*;

@Configuration
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

    @Bean
    public SimpleTextDeleter simpleTextDeleter() {
        return new SimpleTextDeleterVirtual();
    }
}
