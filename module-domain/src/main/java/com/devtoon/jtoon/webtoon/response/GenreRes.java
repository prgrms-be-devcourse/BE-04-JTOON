package com.devtoon.jtoon.webtoon.response;

import com.devtoon.jtoon.webtoon.entity.GenreWebtoon;
import com.devtoon.jtoon.webtoon.entity.enums.Genre;

import lombok.Builder;

@Builder
public record GenreRes(

	Long id,
	Genre type,
	String name
) {

	public static GenreRes from(GenreWebtoon genreWebtoon) {
		return GenreRes.builder()
			.id(genreWebtoon.getId())
			.type(genreWebtoon.getGenre())
			.name(genreWebtoon.getGenre().getText())
			.build();
	}
}
