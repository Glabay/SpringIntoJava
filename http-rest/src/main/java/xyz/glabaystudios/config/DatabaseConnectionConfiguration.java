package xyz.glabaystudios.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(DatabaseConfiguration.class)
public class DatabaseConnectionConfiguration {

    private final DatabaseConfiguration databaseConfiguration;

    @Profile("dev")
    @Bean
    public String devConnectionConfig() {
        System.out.printf("URL: %s%nDriverClassName: %s%nUsername: %s%nPassword: %s%n",
                databaseConfiguration.getUrl(),
                databaseConfiguration.getDriverClassName(),
                databaseConfiguration.getUsername(),
                databaseConfiguration.getPassword()
        );
        return "Database Config for Dev profile";
    }

    @Profile("prod")
    @Bean
    public String prodConnectionConfig() {
        System.out.printf("URL: %s%nDriverClassName: %s%nUsername: %s%nPassword: %s%n",
                databaseConfiguration.getUrl(),
                databaseConfiguration.getDriverClassName(),
                databaseConfiguration.getUsername(),
                databaseConfiguration.getPassword()
        );
        return "Database Config for Prod profile";
    }
}
