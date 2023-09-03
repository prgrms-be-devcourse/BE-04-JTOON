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
public enum LoginType {
	LOCAL,
	NAVER,
	GOOGLE;

	private static final Map<String, LoginType> LOGIN_TYPE_MAP;

	static {
		LOGIN_TYPE_MAP = Collections.unmodifiableMap(
			Arrays.stream(LoginType.values())
				.collect(Collectors.toMap(LoginType::name, loginType -> loginType))
		);
	}

	public static LoginType generate(String loginType) {
		return Optional.ofNullable(LOGIN_TYPE_MAP.get(loginType))
			.orElseThrow(() -> new MemberException(ExceptionCode.MEMBER_LOGIN_TYPE_INVALID_FORMAT));
	}
}
