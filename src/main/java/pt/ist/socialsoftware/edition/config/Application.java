package pt.ist.socialsoftware.edition.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.connect.web.SignInAdapter;

import pt.ist.socialsoftware.edition.security.LdoDSignInAdapter;
import pt.ist.socialsoftware.edition.utils.Bootstrap;

@PropertySource({ "classpath:application.properties", "classpath:secrete.properties" })
@ComponentScan(basePackages = "pt.ist.socialsoftware.edition")
@SpringBootApplication
@EnableAutoConfiguration(exclude = { SocialWebAutoConfiguration.class }) //
// (exclude
// =
// {
// DataSourceAutoConfiguration.class
// })
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
		Bootstrap.initDatabase();
	}

	@Bean
	public SignInAdapter signInAdapter() {
		return new LdoDSignInAdapter(new HttpSessionRequestCache());
	}

	// @Bean // (destroyMethod = "shutdown")
	// public DataSource dataSource() {
	// DriverManagerDataSource mysqldataSource = new DriverManagerDataSource();
	// mysqldataSource.setDriverClassName("com.mysql.jdbc.Driver");
	// mysqldataSource.setUrl("jdbc:mysql://localhost:3306/edition_social");
	// mysqldataSource.setUsername("root");
	// mysqldataSource.setPassword("");
	// return mysqldataSource;
	// }

	// @Bean
	// public PlatformTransactionManager transactionManager() {
	// return new DataSourceTransactionManager(dataSource());
	// }
	//
	// @Bean
	// public DataSourceInitializer dataSourceInitializer(
	// final DataSource dataSource) {
	// final DataSourceInitializer initializer = new DataSourceInitializer();
	// initializer.setDataSource(dataSource);
	// initializer.setDatabasePopulator(databasePopulator());
	// return initializer;
	// }
	//
	// private DatabasePopulator databasePopulator() {
	// ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
	// populator.addScript(
	// new ClassPathResource("JdbcUsersConnectionRepository.sql",
	// JdbcUsersConnectionRepository.class));
	// return populator;
	// }

}
