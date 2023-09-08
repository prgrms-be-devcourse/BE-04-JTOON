package com.devtoon.jtoon.global.util;

import static java.util.Objects.*;

import java.util.function.Function;

import com.querydsl.core.types.dsl.BooleanExpression;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DynamicQuery {

	public static <T> BooleanExpression generateEq(T value, Function<T, BooleanExpression> function) {
		if (isNull(value)) {
			return null;
		}

		return function.apply(value);
	}
}
