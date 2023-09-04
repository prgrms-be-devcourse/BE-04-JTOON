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

	ALL(0),
	AGE_12(12),
	AGE_15(15),
	AGE_19(19);

	private final int age;

	private static final Map<Integer, AgeLimit> AGE_LIMIT_MAP;

	static {
		AGE_LIMIT_MAP = Collections.unmodifiableMap(Arrays.stream(values())
			.collect(Collectors.toMap(AgeLimit::getAge, Function.identity())));
	}

	public static AgeLimit from(int age) {
		return Optional.ofNullable(AGE_LIMIT_MAP.get(age))
			.orElseThrow(() -> new RuntimeException("올바른 연령가 타입이 아닙니다."));
	}
}
