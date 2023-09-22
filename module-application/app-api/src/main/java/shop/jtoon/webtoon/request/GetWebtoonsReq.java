package shop.jtoon.webtoon.request;

import lombok.Builder;
import shop.jtoon.entity.enums.DayOfWeek;

@Builder
public record GetWebtoonsReq(
	DayOfWeek day,
	String keyword
) {
}
