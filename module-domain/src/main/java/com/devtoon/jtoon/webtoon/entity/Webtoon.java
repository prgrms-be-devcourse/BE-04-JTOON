package com.devtoon.jtoon.webtoon.entity;

import static java.util.Objects.*;

import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import com.devtoon.jtoon.global.common.BaseTimeEntity;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.webtoon.entity.converter.AgeLimitConverter;
import com.devtoon.jtoon.webtoon.entity.converter.DayOfWeekConverter;
import com.devtoon.jtoon.webtoon.entity.converter.GenreConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

	@Convert(converter = DayOfWeekConverter.class)
	@Column(name = "day_of_week", nullable = false, length = 30)
	private Set<DayOfWeek> dayOfWeeks;

	@Convert(converter = GenreConverter.class)
	@Column(name = "genre", nullable = false, length = 30)
	private Set<Genre> genres;

	@Convert(converter = AgeLimitConverter.class)
	@Column(name = "age_limit", nullable = false)
	private AgeLimit ageLimit;

	@ColumnDefault("'default thumbnail url'")
	@Column(name = "thumbnail_url", nullable = false, length = 65535)
	private String thumbnailUrl = "default thumbnail url";

	@Column(name = "cookie_count", nullable = false)
	private int cookieCount;

	@Column(name = "interest_count", nullable = false)
	private int interestCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id", nullable = false)
	private Member author;

	@Builder
	private Webtoon(
		String title,
		String description,
		Set<DayOfWeek> dayOfWeeks,
		Set<Genre> genres,
		AgeLimit ageLimit,
		String thumbnailUrl,
		int cookieCount,
		Member author
	) {
		if (cookieCount < 0) {
			throw new RuntimeException("cookieCount is negative number");
		}

		this.title = requireNonNull(title, "title is null");
		this.description = requireNonNull(description, "description is null");
		this.dayOfWeeks = requireNonNull(dayOfWeeks, "dayOfWeeks is null");
		this.genres = requireNonNull(genres, "genres is null");
		this.ageLimit = requireNonNull(ageLimit, "ageLimit is null");
		this.thumbnailUrl = thumbnailUrl;
		this.cookieCount = cookieCount;
		this.author = requireNonNull(author, "author is null");
	}
}
