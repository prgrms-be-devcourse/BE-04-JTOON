package com.devtoon.jtoon.webtoon.entity;

import static java.util.Objects.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.devtoon.jtoon.global.common.BaseTimeEntity;

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

@Entity
@Getter
@Table(name = "episodes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Episode extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "episode_id")
	private Long id;

	@ColumnDefault("1")
	@Column(name = "no", nullable = false)
	private int no;

	@Column(name = "title", nullable = false, length = 30)
	private String title;

	@Column(name = "main_url", nullable = false, length = 500)
	private String mainUrl;

	@ColumnDefault("'default thumbnail url'")
	@Column(name = "thumbnail_url", nullable = false, length = 500)
	private String thumbnailUrl = "default thumbnail url";

	@ColumnDefault("1")
	@Column(name = "has_comment", nullable = false)
	private boolean hasComment;

	@Column(name = "opened_at", nullable = false)
	private LocalDateTime openedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "webtoon_id", nullable = false)
	private Webtoon webtoon;

	@Builder
	private Episode(
		int no,
		String title,
		String mainUrl,
		String thumbnailUrl,
		boolean hasComment,
		LocalDateTime openedAt,
		Webtoon webtoon
	) {
		if (no <= 0) {
			throw new RuntimeException("number is zero or negative number");
		}

		this.no = no;
		this.title = requireNonNull(title, "title is null");
		this.mainUrl = requireNonNull(mainUrl, "mainUrl is null");
		this.thumbnailUrl = thumbnailUrl;
		this.hasComment = hasComment;
		this.openedAt = requireNonNull(openedAt, "openedAt is null");
		this.webtoon = requireNonNull(webtoon, "webtoon is null");
	}
}
