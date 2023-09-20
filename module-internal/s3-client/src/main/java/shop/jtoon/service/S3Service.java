package shop.jtoon.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.UploadImageDto;
import shop.jtoon.util.S3Client;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Client s3Client;

	public String uploadImage(UploadImageDto dto) {
		String key = dto.toKey();
		return s3Client.uploadImage(key, dto.image());
	}

	public void deleteImage(String imageUrl) {
		s3Client.delete(imageUrl);
	}
}
