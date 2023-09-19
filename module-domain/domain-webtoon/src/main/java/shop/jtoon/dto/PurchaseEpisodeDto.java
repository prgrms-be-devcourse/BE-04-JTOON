package shop.jtoon.dto;

import lombok.Builder;
import shop.jtoon.entity.Episode;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PurchasedEpisode;

@Builder
public record PurchaseEpisodeDto(
	Long memberId,
	Long episodeId
) {

	public static PurchaseEpisodeDto of(Long memberId, Long episodeId) {
		return PurchaseEpisodeDto.builder()
			.memberId(memberId)
			.episodeId(episodeId)
			.build();
	}

	public PurchasedEpisode toEntity() {
		return PurchasedEpisode.builder()
			.member(Member.createOfId(memberId))
			.episode(Episode.createOfId(episodeId))
			.build();
	}
}
