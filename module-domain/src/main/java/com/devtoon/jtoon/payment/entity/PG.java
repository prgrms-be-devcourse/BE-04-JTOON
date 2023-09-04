package com.devtoon.jtoon.payment.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PG {
	KCP("KG_이니시스");

	private String name;

	private static final Map<String, PG> PG_MAP;

	static {
		PG_MAP = Collections.unmodifiableMap(Arrays.stream(values())
			.collect(Collectors.toMap(PG::getName, Function.identity())));
	}

	public static PG from(String name) {
		return Optional.ofNullable(PG_MAP.get(name))
			.orElseThrow(() -> new RuntimeException("현재 서버에 등록되지 않은 Payment Gateway 입니다."));
	}
}
