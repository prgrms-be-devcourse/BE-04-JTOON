package shop.jtoon.response;

import lombok.Builder;
import shop.jtoon.entity.Episode;

@Builder
public record EpisodeInfoRes(
	String mainUrl
) {

	public static EpisodeInfoRes from(Episode episode) {
		return EpisodeInfoRes.builder()
			.mainUrl(episode.getMainUrl())
			.build();
	}
}
