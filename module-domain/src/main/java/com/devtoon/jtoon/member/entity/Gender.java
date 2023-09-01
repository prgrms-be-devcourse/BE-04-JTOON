package com.devtoon.jtoon.member.entity;

import com.devtoon.jtoon.exception.ExceptionCode;
import com.devtoon.jtoon.exception.MemberException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Gender {
	MALE, FEMALE;

	private static final Map<String, Gender> GENDER_MAP;

	static {
		GENDER_MAP = new HashMap<>();
		Arrays.stream(Gender.values())
			.forEach(gender -> GENDER_MAP.put(gender.name(), gender));
	}

	public static Gender generate(String gender) {
		return Optional.ofNullable(GENDER_MAP.get(gender))
			.orElseThrow(() -> new MemberException(ExceptionCode.MEMBER_GENDER_INVALID_FORMAT));
	}
}