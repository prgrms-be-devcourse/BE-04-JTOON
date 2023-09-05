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

	//TODO 확장자 포함 + 파일명 변경
	public String upload(ImageType imageType, String webtoonTitle, MultipartFile image) {
		String fileName = UUID.randomUUID().toString();
		return uploadInternal(imageType, webtoonTitle, fileName, image);
	}

	public String upload(ImageType imageType, String webtoonTitle, int no, MultipartFile image) {
		UUID uuid = UUID.randomUUID();
		String fileName = String.format("%04d_", no) + uuid;

		return uploadInternal(imageType, webtoonTitle, fileName, image);
	}

	private String uploadInternal(ImageType imageType, String webtoonTitle, String fileName, MultipartFile image) {
		String key = imageType.getPath(webtoonTitle, fileName);
		s3Uploader.upload(key, image);

		return IMAGE_URL + key;
	}
}
