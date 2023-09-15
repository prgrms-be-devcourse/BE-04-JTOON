package shop.jtoon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.UploadImageDto;
import shop.jtoon.util.S3Uploader;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Uploader s3Uploader;

	@Value("${spring.cloud.aws.cloud-front.url}")
	private String IMAGE_URL;

	public String uploadImage(UploadImageDto request) {
		String key = request.toKey();
		s3Uploader.uploadImage(key, request.image());

		return IMAGE_URL + key;
	}
}
