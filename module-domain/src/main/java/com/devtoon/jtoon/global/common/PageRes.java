package com.devtoon.jtoon.global.common;

import java.util.List;

import lombok.Builder;

@Builder
public record PageRes<T>(
	long totalCount,
	List<T> items
) {

	public static <T> PageRes<T> from(long totalCount, List<T> items) {
		return PageRes.<T>builder()
			.totalCount(totalCount)
			.items(items)
			.build();
	}
}
