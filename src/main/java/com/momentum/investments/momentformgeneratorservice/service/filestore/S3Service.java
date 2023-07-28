package com.momentum.investments.momentformgeneratorservice.service.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.exception.FileDownloadException;
import com.momentum.investments.momentformgeneratorservice.exception.FileUploadException;
import com.momentum.investments.momentformgeneratorservice.exception.GenericException;
import com.momentum.investments.momentformgeneratorservice.oap.IPerformanceMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class S3Service implements IFileStoreManager {

    private final AmazonS3 s3Client;
    private final String csvBucket;
    private final String pdfBucket;

    private final Map<FileType, String> bucketMap;

    public S3Service(@Autowired AmazonS3 s3Client, @Value("${file-manager.aws.upload-bucket}") String csvBucket,
                     @Value("${file-manager.aws.output-bucket}") String pdfBucket) {
        this.s3Client = s3Client;
        this.csvBucket = csvBucket;
        this.pdfBucket = pdfBucket;
        this.bucketMap = Map.of(FileType.PDF, pdfBucket, FileType.CSV, csvBucket);
    }

    @Override
    public InputStream getFileInputStream(final FileType fileType, final String storageId) {

        try {
            log.info("Downloading an object for object" + storageId);
            var bucket =  bucketMap.get(fileType);
            return s3Client.getObject(new GetObjectRequest(bucket, storageId)).getObjectContent();

        } catch (AmazonServiceException e) {
            log.error("AmazonServiceException " + e);
            throw new FileDownloadException("We failed to download the file successfully, please try again later(556)");
        } catch (SdkClientException e) {
            log.error("SdkClientException " + e);
            throw new FileDownloadException("We failed to download the file successfully, please try again later(557)");
        }
    }
    @Override
    @IPerformanceMonitor
    public String uploadFile(File file) {

        var key = UUID.randomUUID().toString();
        try {
            PutObjectRequest request = new PutObjectRequest(pdfBucket, key.toString(), file);
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
        return key;
    }

    @Override
    public List<String> getListOfExistingFiles() {

        List<S3ObjectSummary> objects = s3Client.listObjects(csvBucket).getObjectSummaries();
        return objects.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }
}
