package com.tourplanner.backend.service.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3FileUploadService {

    private final S3Client s3Client;

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    public String uploadFileToS3(InputStream dataStream, String filename, long contentLength) {
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(filename)
                        .build(),
                RequestBody.fromInputStream(dataStream, contentLength));

        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(filename)).toExternalForm();
    }
}
