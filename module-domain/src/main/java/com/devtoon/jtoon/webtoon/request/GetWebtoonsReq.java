package com.devtoon.jtoon.webtoon.request;

import com.devtoon.jtoon.webtoon.entity.enums.DayOfWeek;

public record GetWebtoonsReq(
	DayOfWeek day,
	String keyword
) {
}
