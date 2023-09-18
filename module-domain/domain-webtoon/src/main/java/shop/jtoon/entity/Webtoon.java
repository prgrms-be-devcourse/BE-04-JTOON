package shop.jtoon.entity;

import static java.util.Objects.*;
import static shop.jtoon.type.ErrorStatus.*;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.jtoon.entity.enums.AgeLimit;
import shop.jtoon.exception.InvalidRequestException;

@Entity
@Getter
@Table(name = "webtoons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Webtoon extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "webtoon_id")
	private Long id;

	@Column(name = "title", nullable = false, unique = true, length = 30)
	private String title;

	@Column(name = "description", nullable = false, length = 400)
	private String description;

	@Column(name = "age_limit", nullable = false)
	private AgeLimit ageLimit;

	@ColumnDefault("'default thumbnail url'")
	@Column(name = "thumbnail_url", nullable = false, length = 500)
	private String thumbnailUrl = "default thumbnail url";

	@Column(name = "cookie_count", nullable = false)
	private int cookieCount;

	@Column(name = "favorite_count", nullable = false)
	private int favoriteCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id", nullable = false)
	private Member author;

	@Builder
	private Webtoon(
		String title,
		String description,
		AgeLimit ageLimit,
		String thumbnailUrl,
		int cookieCount,
		Member author
	) {

		this.title = requireNonNull(title, WEBTOON_TITLE_IS_NULL.getMessage());
		this.description = requireNonNull(description, WEBTOON_DESCRIPTION_IS_NULL.getMessage());
		this.ageLimit = requireNonNull(ageLimit, WEBTOON_AGE_LIMIT_IS_NULL.getMessage());
		this.thumbnailUrl = thumbnailUrl;
		this.cookieCount = validateCookieCount(cookieCount);
		this.author = requireNonNull(author, WEBTOON_AUTHOR_IS_NULL.getMessage());
	}

	public static Webtoon createOfId(Long webtoonId) {
		Webtoon webtoon = new Webtoon();
		webtoon.id = webtoonId;

		return webtoon;
	}

	private int validateCookieCount(int cookieCount) {
		if (cookieCount < 0) {
			throw new InvalidRequestException(COOKIE_COUNT_NOT_NEGATIVE);
		}

		return cookieCount;
	}
}
