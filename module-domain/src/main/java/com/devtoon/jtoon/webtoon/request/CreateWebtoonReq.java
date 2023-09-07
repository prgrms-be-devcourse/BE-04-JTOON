package com.devtoon.jtoon.webtoon.request;

import java.util.Set;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.webtoon.entity.AgeLimit;
import com.devtoon.jtoon.webtoon.entity.DayOfWeek;
import com.devtoon.jtoon.webtoon.entity.Genre;
import com.devtoon.jtoon.webtoon.entity.Webtoon;

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

	public Webtoon toEntity(Member member, String thumbnailUrl) {
		return Webtoon.builder()
			.title(this.title)
			.description(this.description)
			.dayOfWeeks(this.dayOfWeeks)
			.genres(this.genres)
			.ageLimit(this.ageLimit)
			.thumbnailUrl(thumbnailUrl)
			.cookieCount(this.cookieCount)
			.author(member)
			.build();
	}
}
