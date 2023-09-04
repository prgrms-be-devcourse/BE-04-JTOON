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
public enum Genre {

	ROMANCE("로맨스");

	private final String value;

	private static final Map<String, Genre> GENRE_MAP;

	static {
		GENRE_MAP = Collections.unmodifiableMap(
			Arrays.stream(values())
				.collect(Collectors.toMap(Genre::getValue, Function.identity()))
		);
	}

	public static Genre from(String value) {
		return Optional.ofNullable(GENRE_MAP.get(value))
			.orElseThrow(() -> new RuntimeException("올바른 장르 타입이 아닙니다."));
	}
}
