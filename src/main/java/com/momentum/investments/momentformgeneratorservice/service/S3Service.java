package com.momentum.investments.momentformgeneratorservice.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.momentum.investments.momentformgeneratorservice.exception.FileUploadException;
import com.momentum.investments.momentformgeneratorservice.exception.GenericException;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class S3Service implements IFileManager {

    private final AmazonS3 s3Client;
    private final String csvBucket;
    private final String pdfBucket;

    public S3Service(@Autowired AmazonS3 s3Client, @Value("${file-manager.aws.upload-bucket}") String csvBucket,
                     @Value("${file-manager.aws.output-bucket}") String pdfBucket) {
        this.s3Client = s3Client;
        this.csvBucket = csvBucket;
        this.pdfBucket = pdfBucket;
    }

    @Override
    public InputStream getFileInputStream(final FileLog fileLog) {

        try {
            log.info("Downloading an object for object" + fileLog);
            return s3Client.getObject(new GetObjectRequest(csvBucket, fileLog.getStorageId().toString())).getObjectContent();

        } catch (AmazonServiceException e) {
            log.error("AmazonServiceException " + e);
            throw new FileUploadException("We failed to download the file successfully, please try again later(556)");
        } catch (SdkClientException e) {
            log.error("SdkClientException " + e);
            throw new FileUploadException("We failed to download the file successfully, please try again later(557)");
        }
    }

    @Override
    public void uploadFile(File file) {

        try {
            PutObjectRequest request = new PutObjectRequest(pdfBucket, "fileObjKeyName", file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");
            metadata.addUserMetadata("title", "someTitle");
            request.setMetadata(metadata);
            s3Client.putObject(request);
        } catch (AmazonServiceException e) {
            throw new FileUploadException("We failed to upload the file successfully, please try again later(550)");
        } catch (SdkClientException e) {
            throw new GenericException("We can't process your file right now. Please try again later.(551)");
        } catch (Exception e) {
            log.error("generic error", e);
            throw new GenericException("We can't process your file right now. Please try again later.(552)");
        }
    }

    @Override
    public List<String> getListOfExistingFiles() {

        List<S3ObjectSummary> objects = s3Client.listObjects(csvBucket).getObjectSummaries();
        return objects.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }
}
