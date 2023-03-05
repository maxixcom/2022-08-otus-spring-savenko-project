package io.github.maxixcom.otus.movies.backend.service.image;

import io.github.maxixcom.otus.movies.backend.config.AwsS3BucketProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageStorageServiceS3 implements ImageStorageService, AutoCloseable {
    private final S3Client s3Client;
    private final AwsS3BucketProvider awsS3BucketProvider;

    @Override
    public void store(byte[] data, Path destination, String mediaType) {
        String contentType = Optional.ofNullable(mediaType)
                .orElse(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);

        PutObjectRequest request = PutObjectRequest.builder()
                .key(destination.toString())
                .bucket(awsS3BucketProvider.getBucketName())
                .contentType(contentType)
                .contentLength((long) data.length)
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(data));
    }

    @Override
    public void delete(Path destination) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .key(destination.toString())
                .bucket(awsS3BucketProvider.getBucketName())
                .build();

        s3Client.deleteObject(request);
    }

    @PostConstruct
    void init() {
        log.info("Starting media storage service");
        try {
            CreateBucketRequest.Builder bucketRequest = CreateBucketRequest.builder()
                    .bucket(awsS3BucketProvider.getBucketName());

            s3Client.createBucket(bucketRequest.build());

        } catch (BucketAlreadyOwnedByYouException | BucketAlreadyExistsException exception) {
            log.debug("Bucket already exists", exception);
            log.warn("Bucket [{}] already exists", awsS3BucketProvider.getBucketName());
        } catch (Exception exception) {
            log.error("Creating bucket [{}] error: {}", awsS3BucketProvider.getBucketName(), exception.getMessage());
        }
    }

    @PreDestroy
    @Override
    public void close() {
        log.info("Closing media storage service");
        s3Client.close();
    }
}
