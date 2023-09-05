package com.devtoon.jtoon.common;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FileName {

	private final String value;

	public static FileName forWebtoon() {
		return new FileName(UUID.randomUUID().toString());
	}

	public static FileName forEpisode(int no) {
		return new FileName(String.format("%04d", no) + UUID.randomUUID());
	}
}
