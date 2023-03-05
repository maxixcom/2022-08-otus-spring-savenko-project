package io.github.maxixcom.otus.movies.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties {
    private String key;
    private long expirationSec;
    private String issuer;
    private String audience;
}
