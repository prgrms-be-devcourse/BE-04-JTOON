package com.devtoon.jtoon.request;

import org.springframework.web.multipart.MultipartFile;

import com.devtoon.jtoon.common.FileName;
import com.devtoon.jtoon.common.ImageType;

import lombok.Builder;

@Builder
public record UploadImageReq(
	ImageType imageType,
	String webtoonTitle,
	FileName fileName,
	MultipartFile image
) {

	public String toKey() {
		return imageType.getPath(webtoonTitle, fileName.getValue());
	}
}