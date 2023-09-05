package com.devtoon.jtoon.webtoon.request;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.devtoon.jtoon.webtoon.entity.Episode;
import com.devtoon.jtoon.webtoon.entity.Webtoon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateEpisodeReq(
	@Min(1) int no,
	@NotBlank @Size(max = 30) String title,
	boolean hasComment,
	@NotNull LocalDateTime openedAt,
	@NotNull MultipartFile mainImage,
	MultipartFile thumbnailImage
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
}
