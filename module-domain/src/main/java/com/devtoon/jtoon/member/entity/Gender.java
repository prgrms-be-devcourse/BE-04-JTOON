package com.devtoon.jtoon.member.entity;

import com.devtoon.jtoon.exception.ExceptionCode;
import com.devtoon.jtoon.exception.MemberException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Gender {
	MALE, FEMALE;

	private static final Map<String, Gender> GENDER_MAP;

	static {
		GENDER_MAP = Collections.unmodifiableMap(
			Arrays.stream(Gender.values())
				.collect(Collectors.toMap(Enum::name, gender -> gender)));
	}

	public static Gender generate(String gender) {
		return Optional.ofNullable(GENDER_MAP.get(gender))
			.orElseThrow(() -> new MemberException(ExceptionCode.MEMBER_GENDER_INVALID_FORMAT));
	}
}
