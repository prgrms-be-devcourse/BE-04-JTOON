package shop.jtoon.entity;

import static java.util.Objects.*;
import static shop.jtoon.type.ErrorStatus.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import shop.jtoon.entity.enums.Genre;

@Entity
@Getter
@Table(name = "genre_webtoons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenreWebtoon extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "genre_webtoon_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "genre", nullable = false, length = 20)
	private Genre genre;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "webtoon_id", nullable = false)
	private Webtoon webtoon;

	@Builder
	private GenreWebtoon(Genre genre, Webtoon webtoon) {
		this.genre = requireNonNull(genre, WEBTOON_GENRE_IS_NULL.getMessage());
		this.webtoon = requireNonNull(webtoon, WEBTOON_IS_NULL.getMessage());
	}
}
