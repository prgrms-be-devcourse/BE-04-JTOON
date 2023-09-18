package shop.jtoon.dto;

import lombok.Builder;
import shop.jtoon.entity.Episode;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PurchasedEpisode;

@Builder
public record PurchaseEpisodeDto(
	Member member,
	Long episodeId
) {

	public static PurchaseEpisodeDto of(Member member, Long episodeId) {
		return PurchaseEpisodeDto.builder()
			.member(member)
			.episodeId(episodeId)
			.build();
	}

	public PurchasedEpisode toEntity() {
		return PurchasedEpisode.builder()
			.member(member)
			.episode(Episode.createOfId(episodeId))
			.build();
	}
}
