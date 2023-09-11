package com.devtoon.jtoon.util;

import static com.devtoon.jtoon.error.model.ErrorStatus.*;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.devtoon.jtoon.error.exception.InvalidRequestException;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3Uploader {

	private final S3Template s3Template;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String BUCKET;

	public void upload(String key, MultipartFile file) {
		try {
			s3Template.upload(
				BUCKET,
				key,
				file.getInputStream(),
				ObjectMetadata.builder().contentType("image/png").build()
			);
		} catch (IOException e) {
			throw new InvalidRequestException(S3_UPLOAD_FAIL);
		}
	}

	//TODO 업로드는 성공했지만, 비즈니스 로직 실패 시 delete 처리 추가
}
