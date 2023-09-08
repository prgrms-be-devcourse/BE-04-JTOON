package com.devtoon.jtoon.webtoon.response;

import com.devtoon.jtoon.webtoon.entity.Episode;

import lombok.Builder;

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
