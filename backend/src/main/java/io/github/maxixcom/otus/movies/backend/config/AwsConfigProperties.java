package io.github.maxixcom.otus.movies.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws")
public class AwsConfigProperties implements AwsS3BucketProvider {
    private String endpointUrl = "http://127.0.0.1:9000";
    private String accessKey;
    private String secretKey;
    private String bucketName = "tmp";
    private String region = "aws-global";

    @Override
    public String getBucketName() {
        return this.bucketName;
    }
}
