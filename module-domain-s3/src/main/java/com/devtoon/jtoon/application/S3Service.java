package com.devtoon.jtoon.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.devtoon.jtoon.request.UploadImageReq;
import com.devtoon.jtoon.util.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Uploader s3Uploader;

	@Value("${spring.cloud.aws.cloud-front.url}")
	private String IMAGE_URL;

	public String upload(UploadImageReq request) {
		String key = request.toKey();
		s3Uploader.upload(key, request.image());

		return IMAGE_URL + key;
	}
}
