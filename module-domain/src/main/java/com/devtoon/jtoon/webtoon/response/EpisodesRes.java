package com.devtoon.jtoon.webtoon.response;

import java.time.format.DateTimeFormatter;

import com.devtoon.jtoon.webtoon.entity.Episode;

import lombok.Builder;

@Builder
public record EpisodesRes(
	Long episodeId,
	int no,
	String title,
	String thumbnailUrl,
	String openedAt,
	Long webtoonId
) {

	public static EpisodesRes from(Episode episode) {
		return EpisodesRes.builder()
			.episodeId(episode.getId())
			.no(episode.getNo())
			.title(episode.getTitle())
			.thumbnailUrl(episode.getThumbnailUrl())
			.openedAt(episode.getOpenedAt().format(DateTimeFormatter.ofPattern("yy.MM.dd")))
			.webtoonId(episode.getWebtoon().getId())
			.build();
	}
}
