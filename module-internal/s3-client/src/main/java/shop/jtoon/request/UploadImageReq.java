package shop.jtoon.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import shop.jtoon.common.FileName;
import shop.jtoon.common.ImageType;

@Builder
public record UploadImageReq(
	ImageType imageType,
	String webtoonTitle,
	FileName fileName,
	MultipartFile image
) {

	public static UploadImageReq of(ImageType imageType, String webtoonTitle, FileName fileName, MultipartFile image) {
		return UploadImageReq.builder()
			.imageType(imageType)
			.webtoonTitle(webtoonTitle)
			.fileName(fileName)
			.image(image)
			.build();
	}

	public String toKey() {
		return imageType.getPath(webtoonTitle, fileName.getValue());
	}
}
