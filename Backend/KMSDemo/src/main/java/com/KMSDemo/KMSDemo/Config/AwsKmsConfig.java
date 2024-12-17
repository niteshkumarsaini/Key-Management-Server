package com.KMSDemo.KMSDemo.Config;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.regions.Regions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsKmsConfig {

    @Value("${aws.config.accessKey}")
    private String awsAccessKey;  // Set your AWS access key here
    
    @Value("${aws.config.secretKey}")
    private String awsSecretKey;  // Set your AWS secret key here
    
    @Value("${aws.config.region}")
    private String awsRegion;  // Specify your AWS region

    /**
     * Configures the AWS KMS client for encryption and decryption operations.
     * 
     * @return AWSKMS client
     */
    @Bean
    public AWSKMS awsKmsClient() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

        return AWSKMSClientBuilder.standard()
                .withRegion(Regions.fromName(awsRegion)) // Specify your region
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
