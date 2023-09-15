package shop.jtoon.webtoon.request;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import shop.jtoon.common.FileName;
import shop.jtoon.common.ImageType;
import shop.jtoon.dto.CreateWebtoonDto;
import shop.jtoon.dto.UploadImageDto;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.enums.AgeLimit;
import shop.jtoon.entity.enums.DayOfWeek;
import shop.jtoon.entity.enums.Genre;

public record CreateWebtoonReq(
	@NotBlank String title,
	@NotBlank String description,
	@NotNull Set<DayOfWeek> dayOfWeeks,
	@NotNull Set<Genre> genres,
	@NotNull AgeLimit ageLimit,
	@Min(0) int cookieCount
) {

	public CreateWebtoonDto toDto(Member member, String thumbnailUrl) {
		return CreateWebtoonDto.builder()
			.member(member)
			.thumbnailUrl(thumbnailUrl)
			.title(title)
			.description(description)
			.dayOfWeeks(dayOfWeeks)
			.genres(genres)
			.ageLimit(ageLimit)
			.cookieCount(cookieCount)
			.build();
	}

	public UploadImageDto toUploadImageDto(ImageType imageType, MultipartFile image) {
		return UploadImageDto.builder()
			.imageType(imageType)
			.webtoonTitle(title)
			.fileName(FileName.forWebtoon())
			.image(image)
			.build();
	}
}
