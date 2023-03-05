package io.github.maxixcom.otus.movies.backend.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;
import java.util.Optional;

@EnableConfigurationProperties({AwsConfigProperties.class})
@Configuration
public class AwsConfig {
    @Bean
    public S3Client s3Client(AwsConfigProperties properties) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                properties.getAccessKey(),
                properties.getSecretKey()
        );

        S3ClientBuilder builder = S3Client.builder()
                .forcePathStyle(true)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(properties.getRegion()));

        Optional.ofNullable(properties.getEndpointUrl())
                .map(URI::create)
                .ifPresent(builder::endpointOverride);

        return builder.build();
    }


}
