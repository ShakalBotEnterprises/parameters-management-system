package mavmi.parameters_management_system.server.database.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("mavmi.parameters_management_system.server.database.*")
@EntityScan("mavmi.parameters_management_system.server.database.*")
@EnableJpaRepositories("mavmi.parameters_management_system.server.database.*")
public class DatabaseConfiguration {

}
