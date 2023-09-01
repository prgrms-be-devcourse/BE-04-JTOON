package com.devtoon.jtoon.member.entity;

import com.devtoon.jtoon.exception.ExceptionCode;
import com.devtoon.jtoon.exception.MemberException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum LoginType {
	LOCAL,
	NAVER,
	GOOGLE;

	private static final Map<String, LoginType> LOGIN_TYPE_MAP;

	static {
		LOGIN_TYPE_MAP = new HashMap<>();
		Arrays.stream(LoginType.values())
			.forEach(loginType -> LOGIN_TYPE_MAP.put(loginType.name(), loginType));
	}

	public static LoginType generate(String loginType) {
		return Optional.ofNullable(LOGIN_TYPE_MAP.get(loginType))
			.orElseThrow(() -> new MemberException(ExceptionCode.MEMBER_LOGIN_TYPE_INVALID_FORMAT));
	}
}
