package com.devtoon.jtoon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

	@Value("${spring.cloud.aws.credentials.access-key}")
	private String ACCESS_KEY_ID;

	@Value("${spring.cloud.aws.credentials.secret-key}")
	private String SECRET_ACCESS_KEY;

	@Value("${spring.cloud.aws.region.static}")
	private String REGION;

	@Bean
	public S3Client s3Client() {
		AwsCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY_ID, SECRET_ACCESS_KEY);

		return S3Client.builder()
			.credentialsProvider(StaticCredentialsProvider.create(credentials))
			.region(Region.of(REGION))
			.build();
	}
}
