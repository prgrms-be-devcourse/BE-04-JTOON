package shop.jtoon.response;

import lombok.Builder;
import shop.jtoon.entity.Episode;

@Builder
public record EpisodeRes(
	String mainUrl
) {

	public static EpisodeRes from(Episode episode) {
		return EpisodeRes.builder()
			.mainUrl(episode.getMainUrl())
			.build();
	}
}
