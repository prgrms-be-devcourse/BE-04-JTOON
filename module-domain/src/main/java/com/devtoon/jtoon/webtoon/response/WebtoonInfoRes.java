package com.devtoon.jtoon.webtoon.response;

import java.util.Set;

import com.devtoon.jtoon.webtoon.entity.AgeLimit;
import com.devtoon.jtoon.webtoon.entity.DayOfWeek;
import com.devtoon.jtoon.webtoon.entity.Genre;
import com.devtoon.jtoon.webtoon.entity.Webtoon;

import lombok.Builder;

@Builder
public record WebtoonInfoRes(
	String title,
	String description,
	Set<DayOfWeek> dayOfWeeks,
	Set<Genre> genres,
	AgeLimit ageLimit,
	String thumbnailUrl,
	int interestCount,
	Long authorId,
	String authorName
) {

	public static WebtoonInfoRes from(Webtoon webtoon) {
		return WebtoonInfoRes.builder()
			.title(webtoon.getTitle())
			.description(webtoon.getDescription())
			.dayOfWeeks(webtoon.getDayOfWeeks())
			.genres(webtoon.getGenres())
			.ageLimit(webtoon.getAgeLimit())
			.thumbnailUrl(webtoon.getThumbnailUrl())
			.interestCount(webtoon.getInterestCount())
			.authorId(webtoon.getAuthor().getId())
			.authorName(webtoon.getAuthor().getNickname())
			.build();
	}
}
