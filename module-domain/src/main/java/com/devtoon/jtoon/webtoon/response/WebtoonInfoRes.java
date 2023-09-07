package com.devtoon.jtoon.webtoon.response;

import java.util.List;

import com.devtoon.jtoon.webtoon.entity.Webtoon;
import com.devtoon.jtoon.webtoon.entity.enums.AgeLimit;

import lombok.Builder;

@Builder
public record WebtoonInfoRes(
	String title,
	String description,
	List<String> dayOfWeeks,
	List<GenreRes> genres,
	AgeLimit ageLimit,
	String thumbnailUrl,
	int favoriteCount,
	Long authorId,
	String authorName
) {

	public static WebtoonInfoRes of(Webtoon webtoon, List<String> dayOfWeeks, List<GenreRes> genres) {
		return WebtoonInfoRes.builder()
			.title(webtoon.getTitle())
			.description(webtoon.getDescription())
			.dayOfWeeks(dayOfWeeks)
			.genres(genres)
			.ageLimit(webtoon.getAgeLimit())
			.thumbnailUrl(webtoon.getThumbnailUrl())
			.favoriteCount(webtoon.getFavoriteCount())
			.authorId(webtoon.getAuthor().getId())
			.authorName(webtoon.getAuthor().getNickname())
			.build();
	}
}
