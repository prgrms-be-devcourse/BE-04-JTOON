package shop.jtoon.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import shop.jtoon.entity.Episode;
import shop.jtoon.entity.Webtoon;

@Builder
public record CreateEpisodeDto(
	Long webtoonId,
	String mainUrl,
	String thumbnailUrl,
	int no,
	@Size(max = 30) String title,
	boolean hasComment,
	LocalDateTime openedAt
) {

	public Episode toEntity() {
		return Episode.builder()
			.no(no)
			.title(title)
			.hasComment(hasComment)
			.openedAt(openedAt)
			.mainUrl(mainUrl)
			.thumbnailUrl(thumbnailUrl)
			.webtoon(Webtoon.createOfId(webtoonId))
			.build();
	}
}
