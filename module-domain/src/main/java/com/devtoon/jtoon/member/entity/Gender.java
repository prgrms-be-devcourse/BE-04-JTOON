package com.devtoon.jtoon.member.entity;

import com.devtoon.jtoon.error.exception.MemberException;
import com.devtoon.jtoon.error.model.ErrorStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

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
public enum Gender {

	MALE("M"),
	FEMALE("F"),
	UNKNOWN("U");

	private String naverGender;
	private static final Map<String, Gender> GENDER_MAP;

	static {
		GENDER_MAP = Collections.unmodifiableMap(
			Arrays.stream(Gender.values())
				.collect(Collectors.toMap(Gender::getNaverGender, Function.identity())));
	}

	public static Gender from(String gender) {
		return Optional.ofNullable(GENDER_MAP.get(gender.toUpperCase()))
			.orElseThrow(() -> new MemberException(ExceptionCode.MEMBER_GENDER_INVALID_FORMAT));
	}
}
