package com.devtoon.jtoon.member.entity;

import com.devtoon.jtoon.error.exception.MemberException;
import com.devtoon.jtoon.error.model.ExceptionStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Gender {
    MALE,
    FEMALE;

    private static final Map<String, Gender> GENDER_MAP;

    static {
        GENDER_MAP = Collections.unmodifiableMap(
                Arrays.stream(Gender.values())
                        .collect(Collectors.toMap(Enum::name, Function.identity())));
    }

    public static Gender from(String gender) {
        return Optional.ofNullable(GENDER_MAP.get(gender))
                .orElseThrow(() -> new MemberException(ExceptionStatus.MEMBER_GENDER_INVALID_FORMAT));
    }
}
