package com.devtoon.jtoon.webtoon.response;

import com.devtoon.jtoon.webtoon.entity.Webtoon;

import lombok.Builder;

@Builder
public record WebtoonItemRes(
	Long webtoonId,
	String title,
	String thumbnailUrl,
	int ageLimit,
	AuthorRes author
) {

	public static WebtoonItemRes from(Webtoon webtoon) {
		return WebtoonItemRes.builder()
			.webtoonId(webtoon.getId())
			.title(webtoon.getTitle())
			.thumbnailUrl(webtoon.getThumbnailUrl())
			.ageLimit(webtoon.getAgeLimit().getValue())
			.author(AuthorRes.from(webtoon.getAuthor()))
			.build();
	}
}
