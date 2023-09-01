package com.devtoon.jtoon.webtoon;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devtoon.jtoon.S3Uploader;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UploadController {

	private final S3Uploader s3Uploader;

	@PostMapping("/")
	public void test(
		@RequestPart(required = false, value = "image") MultipartFile[] files
	) {
		for (MultipartFile file : files) {
			s3Uploader.upload("images", file);
		}
	}
}
