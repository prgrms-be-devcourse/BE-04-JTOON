package com.devtoon.jtoon.webtoon.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DayOfWeek {

	MON("월"),
	TUE("화"),
	WED("수"),
	THU("목"),
	FRI("금"),
	SAT("토"),
	SUN("일");

	private final String name;

	private static final Map<String, DayOfWeek> DAY_OF_WEEK_MAP;

	static {
		DAY_OF_WEEK_MAP = Collections.unmodifiableMap(
			Arrays.stream(values())
				.collect(Collectors.toMap(DayOfWeek::getName, Function.identity())
				)
		);
	}

	public static DayOfWeek from(String name) {
		return Optional.ofNullable(DAY_OF_WEEK_MAP.get(name))
			.orElseThrow(() -> new RuntimeException("올바른 요일 타입이 아닙니다."));
	}
}
