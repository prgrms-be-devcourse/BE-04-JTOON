package com.devtoon.jtoon.webtoon.request;

import java.util.List;
import java.util.Set;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.webtoon.entity.DayOfWeekWebtoon;
import com.devtoon.jtoon.webtoon.entity.GenreWebtoon;
import com.devtoon.jtoon.webtoon.entity.Webtoon;
import com.devtoon.jtoon.webtoon.entity.enums.AgeLimit;
import com.devtoon.jtoon.webtoon.entity.enums.DayOfWeek;
import com.devtoon.jtoon.webtoon.entity.enums.Genre;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateWebtoonReq(
	@NotBlank String title,
	@NotBlank String description,
	@NotNull Set<DayOfWeek> dayOfWeeks,
	@NotNull Set<Genre> genres,
	@NotNull AgeLimit ageLimit,
	@Min(0) int cookieCount
) {

	public Webtoon toWebtoonEntity(Member member, String thumbnailUrl) {
		return Webtoon.builder()
			.title(title)
			.description(description)
			.ageLimit(ageLimit)
			.thumbnailUrl(thumbnailUrl)
			.cookieCount(cookieCount)
			.author(member)
			.build();
	}

	public List<DayOfWeekWebtoon> toDayOfWeekWebtoonEntity(Webtoon webtoon) {
		return dayOfWeeks.stream()
			.map(dayOfWeek -> DayOfWeekWebtoon.builder()
				.dayOfWeek(dayOfWeek)
				.webtoon(webtoon)
				.build()
			)
			.toList();
	}

	public List<GenreWebtoon> toGenreWebtoonEntity(Webtoon webtoon) {
		return genres.stream()
			.map(genre -> GenreWebtoon.builder()
				.genre(genre)
				.webtoon(webtoon)
				.build()
			)
			.toList();
	}
}
