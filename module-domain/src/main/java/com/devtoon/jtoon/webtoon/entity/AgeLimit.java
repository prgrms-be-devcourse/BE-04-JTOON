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
public enum AgeLimit {

	ALL("전체"),
	AGE_12("12세"),
	AGE_15("15세"),
	ADULT("19세");

	private final String value;

	private static final Map<String, AgeLimit> AGE_LIMIT_MAP;

	static {
		AGE_LIMIT_MAP = Collections.unmodifiableMap(
			Arrays.stream(values())
				.collect(Collectors.toMap(AgeLimit::getValue, Function.identity()))
		);
	}

	public static AgeLimit from(String value) {
		return Optional.ofNullable(AGE_LIMIT_MAP.get(value))
			.orElseThrow(() -> new RuntimeException("올바른 연령가 타입이 아닙니다."));
	}
}
