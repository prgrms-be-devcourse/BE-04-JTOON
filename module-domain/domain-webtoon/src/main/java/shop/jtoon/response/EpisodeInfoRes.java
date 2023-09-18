package shop.jtoon.response;

import lombok.Builder;

@Builder
public record EpisodeInfoRes(
	String mainUrl
) {

	public static EpisodeInfoRes from(EpisodeRes episode) {
		return EpisodeInfoRes.builder()
			.mainUrl(episode.mainUrl())
			.build();
	}
}
