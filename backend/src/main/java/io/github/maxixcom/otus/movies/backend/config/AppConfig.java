package io.github.maxixcom.otus.movies.backend.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtConfigProperties.class})
public class AppConfig {
}
