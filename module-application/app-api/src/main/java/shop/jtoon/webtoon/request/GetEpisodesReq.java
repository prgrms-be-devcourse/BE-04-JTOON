package shop.jtoon.webtoon.request;

import lombok.Getter;
import shop.jtoon.dto.GetEpisodesDto;
import shop.jtoon.type.CustomPageRequest;

@Getter
public class GetEpisodesReq extends CustomPageRequest {

	public GetEpisodesDto toDto(Long webtoonId) {
		return GetEpisodesDto.builder()
			.webtoonId(webtoonId)
			.size(getSize())
			.offset(getOffset())
			.build();
	}
}
