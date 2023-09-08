package com.devtoon.jtoon.member.entity;

import com.devtoon.jtoon.error.exception.MemberException;
import com.devtoon.jtoon.error.model.ExceptionCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LoginType {
    LOCAL,
    NAVER,
    GOOGLE;

    private static final Map<String, LoginType> LOGIN_TYPE_MAP;

    static {
        LOGIN_TYPE_MAP = Collections.unmodifiableMap(
                Arrays.stream(LoginType.values())
                        .collect(Collectors.toMap(LoginType::name, Function.identity()))
        );
    }

    public static LoginType from(String loginType) {
        return Optional.ofNullable(LOGIN_TYPE_MAP.get(loginType))
                .orElseThrow(() -> new MemberException(ExceptionCode.MEMBER_LOGIN_TYPE_INVALID_FORMAT));
    }
}
