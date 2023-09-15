package shop.jtoon.dto;

import java.util.List;
import java.util.Set;

import lombok.Builder;
import shop.jtoon.entity.DayOfWeekWebtoon;
import shop.jtoon.entity.GenreWebtoon;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.Webtoon;
import shop.jtoon.entity.enums.AgeLimit;
import shop.jtoon.entity.enums.DayOfWeek;
import shop.jtoon.entity.enums.Genre;

@Builder
public record CreateWebtoonDto(
	Member member,
	String thumbnailUrl,
	String title,
	String description,
	Set<DayOfWeek> dayOfWeeks,
	Set<Genre> genres,
	AgeLimit ageLimit,
	int cookieCount
) {

	public Webtoon toWebtoonEntity() {
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
			.map(dayOfWeek -> DayOfWeekWebtoon.create(dayOfWeek, webtoon))
			.toList();
	}

	public List<GenreWebtoon> toGenreWebtoonEntity(Webtoon webtoon) {
		return genres.stream()
			.map(genre -> GenreWebtoon.create(genre, webtoon))
			.toList();
	}
}
