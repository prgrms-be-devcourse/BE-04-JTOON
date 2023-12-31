package shop.jtoon.util;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.util.CollectionUtils;

import com.querydsl.core.types.dsl.BooleanExpression;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DynamicQuery {

	public static <T> BooleanExpression generateEq(T value, Function<T, BooleanExpression> function) {
		if (Objects.isNull(value)) {
			return null;
		}

		return function.apply(value);
	}

	public static <T> BooleanExpression filterCondition(T condition, Function<T, BooleanExpression> function) {
		T tempCondition = condition;

		if (tempCondition instanceof List<?> c && CollectionUtils.isEmpty(c)) {
			tempCondition = null;
		}

		return Optional.ofNullable(tempCondition)
			.map(function)
			.orElse(null);
	}
}
