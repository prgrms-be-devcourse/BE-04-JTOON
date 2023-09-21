package shop.jtoon.webtoon.request;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import shop.jtoon.common.FileName;
import shop.jtoon.common.ImageType;
import shop.jtoon.dto.UploadImageDto;
import shop.jtoon.entity.Episode;
import shop.jtoon.entity.Webtoon;

public record CreateEpisodeReq(
	@Min(1) int no,
	@NotBlank @Size(max = 30) String title,
	boolean hasComment,
	@NotNull LocalDateTime openedAt
) {

	public Episode toEntity(Webtoon webtoon, String mainUrl, String thumbnailUrl) {
		return Episode.builder()
			.no(no)
			.title(title)
			.hasComment(hasComment)
			.openedAt(openedAt)
			.mainUrl(mainUrl)
			.thumbnailUrl(thumbnailUrl)
			.webtoon(webtoon)
			.build();
	}

	public UploadImageDto toUploadImageDto(ImageType imageType, String webtoonTitle, MultipartFile image) {
		return UploadImageDto.builder()
			.imageType(imageType)
			.webtoonTitle(webtoonTitle)
			.fileName(FileName.forEpisode(no))
			.image(image)
			.build();
	}
}
