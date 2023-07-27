package com.momentum.investments.momentformgeneratorservice.config;


import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = "com.momentum.investments.momentformgeneratorservice")
@EnableJpaRepositories(basePackages = {"com.momentum.investments.momentformgeneratorservice.repository"})
@EntityScan(basePackages = {"com.momentum.investments.momentformgeneratorservice.repository.entity"})
@Configuration
public class AppConfiguration {

    private final String awsRegion;
    private final String awsS3Endpoint;

    public AppConfiguration(@Value("${file-manager.aws.endpoint}") String awsS3Endpoint,
                            @Value("${file-manager.aws.region}") String awsRegion) {

        this.awsRegion = awsRegion;
        this.awsS3Endpoint = awsS3Endpoint;
    }

    @Bean
    public AmazonS3 getAmazonS3() {

        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsS3Endpoint, awsRegion))
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withPathStyleAccessEnabled(true)
                .build();

        return s3Client;
    }
}
