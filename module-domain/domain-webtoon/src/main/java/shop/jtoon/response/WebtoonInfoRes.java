package shop.jtoon.response;

import java.util.List;

import lombok.Builder;
import shop.jtoon.entity.enums.AgeLimit;

@Builder
public record WebtoonInfoRes(
	String title,
	String description,
	List<String> dayOfWeeks,
	List<GenreRes> genres,
	AgeLimit ageLimit,
	String thumbnailUrl,
	int favoriteCount,
	AuthorRes author
) {

	public static WebtoonInfoRes of(WebtoonRes webtoonRes, List<String> dayOfWeeks, List<GenreRes> genres) {
		return WebtoonInfoRes.builder()
			.title(webtoonRes.title())
			.description(webtoonRes.description())
			.dayOfWeeks(dayOfWeeks)
			.genres(genres)
			.ageLimit(webtoonRes.ageLimit())
			.thumbnailUrl(webtoonRes.thumbnailUrl())
			.favoriteCount(webtoonRes.favoriteCount())
			.author(AuthorRes.from(webtoonRes.author()))
			.build();
	}
}
