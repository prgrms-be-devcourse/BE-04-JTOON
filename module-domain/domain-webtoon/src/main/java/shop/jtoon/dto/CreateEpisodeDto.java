package shop.jtoon.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import shop.jtoon.entity.Episode;
import shop.jtoon.entity.Webtoon;
import shop.jtoon.response.WebtoonRes;

@Builder
public record CreateEpisodeDto(
	WebtoonRes webtoonRes,
	String mainUrl,
	String thumbnailUrl,
	int no,
	@Size(max = 30) String title,
	boolean hasComment,
	LocalDateTime openedAt
) {

	public Episode toEpisodeEntity(Webtoon webtoon) {
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

	public Webtoon toWebtoonEntity() {
		return Webtoon.builder()
			.title(webtoonRes.title())
			.description(webtoonRes.description())
			.ageLimit(webtoonRes.ageLimit())
			.thumbnailUrl(webtoonRes.thumbnailUrl())
			.cookieCount(webtoonRes.cookieCount())
			.author(webtoonRes.author())
			.build();
	}
}
