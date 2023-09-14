package shop.jtoon.entity;

import static java.util.Objects.*;
import static shop.jtoon.type.ErrorStatus.*;

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
@Table(name = "purchased_episodes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchasedEpisode extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "purchased_episode_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "episode_id", nullable = false)
	private Episode episode;

	@Builder
	private PurchasedEpisode(Member member, Episode episode) {
		this.member = requireNonNull(member, MEMBER_IS_NULL.getMessage());
		this.episode = requireNonNull(episode, EPISODE_IS_NULL.getMessage());
	}
}
