package shop.jtoon.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.UploadImageDto;
import shop.jtoon.util.S3Manager;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Manager s3Manager;

	public String uploadImage(UploadImageDto dto) {
		String key = dto.toKey();
		return s3Manager.uploadImage(key, dto.image());
	}

	public void deleteImage(String imageUrl) {
		s3Manager.delete(imageUrl);
	}
}
