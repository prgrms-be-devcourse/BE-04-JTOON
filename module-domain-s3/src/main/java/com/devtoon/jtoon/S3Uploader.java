package com.devtoon.jtoon;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3Uploader {

	private final S3Template s3Template;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String BUCKET;

	public void upload(String directory, MultipartFile file) {
		String key = directory + File.separator + UUID.randomUUID();

		try {
			s3Template.upload(
				BUCKET,
				key,
				file.getInputStream(),
				ObjectMetadata.builder().contentType("multipart/form-data").build()
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
