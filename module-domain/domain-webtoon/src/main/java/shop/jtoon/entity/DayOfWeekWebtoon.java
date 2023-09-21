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
import shop.jtoon.entity.enums.DayOfWeek;

@Entity
@Getter
@Table(name = "day_of_week_webtoons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DayOfWeekWebtoon extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "day_of_week_webtoon_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "day_of_week", nullable = false, length = 3)
	private DayOfWeek dayOfWeek;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "webtoon_id", nullable = false)
	private Webtoon webtoon;

	@Builder
	private DayOfWeekWebtoon(DayOfWeek dayOfWeek, Webtoon webtoon) {
		this.dayOfWeek = requireNonNull(dayOfWeek, WEBTOON_DAY_OF_WEEK_IS_NULL.getMessage());
		this.webtoon = requireNonNull(webtoon, WEBTOON_IS_NULL.getMessage());
	}

	public static DayOfWeekWebtoon create(DayOfWeek dayOfWeek, Webtoon webtoon) {
		return DayOfWeekWebtoon.builder()
			.dayOfWeek(dayOfWeek)
			.webtoon(webtoon)
			.build();
	}

	public String getDayOfWeekName() {
		return dayOfWeek.name();
	}
}
