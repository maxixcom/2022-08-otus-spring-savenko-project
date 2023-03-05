package io.github.maxixcom.otus.movies.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "io.github.maxixcom.otus.movies.backend.repository")
public class JpaConfig {
}
