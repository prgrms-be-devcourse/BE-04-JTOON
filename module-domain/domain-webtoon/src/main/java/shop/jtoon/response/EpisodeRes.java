package shop.jtoon.response;

import java.time.LocalDateTime;

import lombok.Builder;
import shop.jtoon.entity.Episode;
import shop.jtoon.entity.Webtoon;

@Builder
public record EpisodeRes(
	Long id,
	int no,
	String title,
	String mainUrl,
	String thumbnailUrl,
	boolean hasComment,
	LocalDateTime openedAt,
	Webtoon webtoon,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {

	public static EpisodeRes from(Episode episode) {
		return EpisodeRes.builder()
			.id(episode.getId())
			.no(episode.getNo())
			.title(episode.getTitle())
			.mainUrl(episode.getMainUrl())
			.thumbnailUrl(episode.getThumbnailUrl())
			.hasComment(episode.isHasComment())
			.openedAt(episode.getOpenedAt())
			.webtoon(episode.getWebtoon())
			.createdAt(episode.getCreatedAt())
			.updatedAt(episode.getUpdatedAt())
			.build();
	}

	public int getCookieCount() {
		return webtoon.getCookieCount();
	}
}
