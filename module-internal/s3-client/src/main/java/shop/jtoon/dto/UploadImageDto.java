package shop.jtoon.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import shop.jtoon.common.FileName;
import shop.jtoon.common.ImageType;

@Builder
public record UploadImageDto(
	ImageType imageType,
	String webtoonTitle,
	FileName fileName,
	MultipartFile image
) {

	public String toKey() {
		return imageType.getPath(webtoonTitle, fileName.getValue());
	}
}
