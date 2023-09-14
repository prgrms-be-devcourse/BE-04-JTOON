package shop.jtoon.response;

import lombok.Builder;
import shop.jtoon.entity.Webtoon;

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
