package shop.jtoon.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.type.ErrorStatus;

@Component
@RequiredArgsConstructor
public class S3Manager {

	private final S3Template s3Template;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String BUCKET;

	@Value("${spring.cloud.aws.s3.url}")
	private String S3_BASE_URL;

	@Value("${spring.cloud.aws.cloud-front.url}")
	private String CLOUD_FRONT_URL;

	public String uploadImage(String key, MultipartFile file) {
		try {
			s3Template.upload(
				BUCKET,
				key,
				file.getInputStream(),
				ObjectMetadata.builder().contentType("image/png").build()
			);

			return CLOUD_FRONT_URL + key;
		} catch (IOException e) {
			throw new InvalidRequestException(ErrorStatus.S3_UPLOAD_FAIL);
		}
	}

	public void delete(String objectUrl) {
		String s3Url = objectUrl.replace(CLOUD_FRONT_URL, S3_BASE_URL);
		s3Template.deleteObject(s3Url);
	}
}
