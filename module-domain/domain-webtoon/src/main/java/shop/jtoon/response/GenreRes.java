package shop.jtoon.response;

import lombok.Builder;
import shop.jtoon.entity.GenreWebtoon;
import shop.jtoon.entity.enums.Genre;

@Builder
public record GenreRes(
	Genre type,
	String name
) {

	public static GenreRes from(GenreWebtoon genreWebtoon) {
		return GenreRes.builder()
			.type(genreWebtoon.getGenre())
			.name(genreWebtoon.getGenre().getText())
			.build();
	}
}
