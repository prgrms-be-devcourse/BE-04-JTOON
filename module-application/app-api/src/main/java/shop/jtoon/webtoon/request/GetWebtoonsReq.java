package shop.jtoon.webtoon.request;

import shop.jtoon.dto.GetWebtoonsDto;
import shop.jtoon.entity.enums.DayOfWeek;

public record GetWebtoonsReq(
	DayOfWeek day,
	String keyword
) {

	public GetWebtoonsDto toDto() {
		return GetWebtoonsDto.builder()
			.day(day)
			.keyword(keyword)
			.build();
	}
}
