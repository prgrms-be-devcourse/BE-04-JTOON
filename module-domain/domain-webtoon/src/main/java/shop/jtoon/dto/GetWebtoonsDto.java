package shop.jtoon.dto;

import lombok.Builder;
import shop.jtoon.entity.enums.DayOfWeek;

@Builder
public record GetWebtoonsDto(
	DayOfWeek day,
	String keyword
) {
}
