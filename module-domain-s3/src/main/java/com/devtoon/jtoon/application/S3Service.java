package com.devtoon.jtoon.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devtoon.jtoon.common.FileName;
import com.devtoon.jtoon.common.ImageType;
import com.devtoon.jtoon.util.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Uploader s3Uploader;

	@Value("${spring.cloud.aws.cloud-front.url}")
	private String IMAGE_URL;

	//TODO 확장자 포함
	public String upload(ImageType imageType, String webtoonTitle, FileName fileName, MultipartFile image) {
		String key = imageType.getPath(webtoonTitle, fileName.getValue());
		s3Uploader.upload(key, image);

		return IMAGE_URL + key;
	}
}
