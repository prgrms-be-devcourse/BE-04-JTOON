package shop.jtoon.entity;

import static java.util.Objects.*;
import static shop.jtoon.type.ErrorStatus.*;

import java.time.LocalDateTime;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.jtoon.exception.InvalidRequestException;

@Entity
@Getter
@Table(name = "episodes", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"webtoon_id", "no"})
})
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

	@Column(name = "thumbnail_url", nullable = false, length = 500)
	private String thumbnailUrl;

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
		this.no = validateEpisodeNumber(no);
		this.title = requireNonNull(title, EPISODE_TITLE_IS_NULL.getMessage());
		this.mainUrl = requireNonNull(mainUrl, EPISODE_MAIN_URL_IS_NULL.getMessage());
		this.thumbnailUrl = requireNonNull(thumbnailUrl, EPISODE_THUMBNAIL_URL_IS_NULL.getMessage());
		this.hasComment = hasComment;
		this.openedAt = requireNonNull(openedAt, EPISODE_OPENED_AT_IS_NULL.getMessage());
		this.webtoon = requireNonNull(webtoon, WEBTOON_IS_NULL.getMessage());
	}

	public static Episode createOfId(Long episodeId) {
		Episode episode = new Episode();
		episode.id = episodeId;

		return episode;
	}

	public int getCookieCount() {
		return webtoon.getCookieCount();
	}

	private int validateEpisodeNumber(int no) {
		if (no <= 0) {
			throw new InvalidRequestException(EPISODE_NUMBER_POSITIVE);
		}

		return no;
	}
}
