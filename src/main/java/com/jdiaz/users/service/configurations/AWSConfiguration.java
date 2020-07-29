package com.jdiaz.users.service.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AWSConfiguration {

	@Value("${config.aws.access-key}")
	private String awsAccessKey;

	@Value("${config.aws.secret-key}")
	private String awsSecretKey;

	@Bean
	public AmazonS3 amazonS3Client() {
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(amazonAWSCredentials())
				.withRegion(Regions.US_EAST_1).build();
		return s3Client;
	}

	@Bean
	@Primary
	public AWSCredentialsProvider amazonAWSCredentials() {
		return new AWSCredentialsProvider() {
			public void refresh() {
			}

			public AWSCredentials getCredentials() {
				return new AWSCredentials() {
					public String getAWSSecretKey() {
						return awsSecretKey;
					}

					public String getAWSAccessKeyId() {
						return awsAccessKey;
					}
				};
			}
		};
	}

}
