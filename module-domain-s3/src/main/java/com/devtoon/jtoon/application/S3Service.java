package com.devtoon.jtoon.application;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devtoon.jtoon.common.ImageType;
import com.devtoon.jtoon.util.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Uploader s3Uploader;

	@Value("${spring.cloud.aws.cloud-front.url}")
	private String IMAGE_URL;

	public String upload(ImageType imageType, MultipartFile image) {
		String uuid = UUID.randomUUID().toString();
		//TODO 확장자 포함 + 파일명 변경
		String key = imageType.getPath(uuid);
		s3Uploader.upload(key, image);

		return IMAGE_URL + key;
	}
}
