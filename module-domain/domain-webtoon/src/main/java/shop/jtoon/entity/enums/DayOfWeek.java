package shop.jtoon.entity.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DayOfWeek {

	MON,
	TUE,
	WED,
	THU,
	FRI,
	SAT,
	SUN;
}
