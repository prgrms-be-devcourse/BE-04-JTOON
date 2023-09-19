package shop.jtoon.response;

import lombok.Builder;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.Webtoon;
import shop.jtoon.entity.enums.AgeLimit;

@Builder
public record WebtoonRes(
	Long id,
	String title,
	String description,
	AgeLimit ageLimit,
	String thumbnailUrl,
	int cookieCount,
	int favoriteCount,
	Member author
) {

	public static WebtoonRes from(Webtoon webtoon) {
		return WebtoonRes.builder()
			.id(webtoon.getId())
			.title(webtoon.getTitle())
			.description(webtoon.getDescription())
			.ageLimit(webtoon.getAgeLimit())
			.thumbnailUrl(webtoon.getThumbnailUrl())
			.cookieCount(webtoon.getCookieCount())
			.favoriteCount(webtoon.getFavoriteCount())
			.author(webtoon.getAuthor())
			.build();
	}
}
