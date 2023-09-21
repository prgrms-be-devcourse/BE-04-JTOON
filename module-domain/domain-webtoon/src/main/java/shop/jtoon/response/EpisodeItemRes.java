package shop.jtoon.response;

import java.time.format.DateTimeFormatter;

import lombok.Builder;
import shop.jtoon.entity.Episode;

@Builder
public record EpisodeItemRes(
	Long episodeId,
	int no,
	String title,
	String thumbnailUrl,
	String openedAt
) {

	public static EpisodeItemRes from(Episode episode) {
		return EpisodeItemRes.builder()
			.episodeId(episode.getId())
			.no(episode.getNo())
			.title(episode.getTitle())
			.thumbnailUrl(episode.getThumbnailUrl())
			.openedAt(episode.getOpenedAt().format(DateTimeFormatter.ofPattern("yy.MM.dd")))
			.build();
	}
}
