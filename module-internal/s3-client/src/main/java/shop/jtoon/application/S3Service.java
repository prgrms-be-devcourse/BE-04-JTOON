package shop.jtoon.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.jtoon.request.UploadImageReq;
import shop.jtoon.util.S3Uploader;

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
