package xyz.glabaystudios.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.datasource")
public class DatabaseConfiguration {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
