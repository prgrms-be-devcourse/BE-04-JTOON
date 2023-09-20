package shop.jtoon.dto;

import lombok.Builder;

@Builder
public record GetEpisodesDto(
	Long webtoonId,
	int size,
	Long offset
) {
}
