package nmtasks.repositories;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
 
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"nmtasks.beans"})
@EnableJpaRepositories(basePackages = {"nmtasks.repositories"})
@EnableTransactionManagement
public class RepoConfig { }