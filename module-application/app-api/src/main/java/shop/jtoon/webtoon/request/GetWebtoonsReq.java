package shop.jtoon.webtoon.request;

import shop.jtoon.entity.enums.DayOfWeek;

public record GetWebtoonsReq(
	DayOfWeek day,
	String keyword
) {
}
