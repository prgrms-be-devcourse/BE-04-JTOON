package com.devtoon.jtoon.paymentinfo.entity;

import com.devtoon.jtoon.error.exception.NotFoundException;
import com.devtoon.jtoon.error.model.ExceptionStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CookieItem {
    COOKIE_ONE("쿠키 10개", BigDecimal.valueOf(1000)),
    COOKIE_TWO("쿠키 20개", BigDecimal.valueOf(2000)),
    COOKIE_THREE("쿠키 30개", BigDecimal.valueOf(3000)),
    COOKIE_FOUR("쿠키 50개", BigDecimal.valueOf(5000)),
    COOKIE_FIVE("쿠키 100개", BigDecimal.valueOf(10000));

    private String itemName;
    private BigDecimal amount;

    private static final Map<String, CookieItem> COOKIE_MAP;

    static {
        COOKIE_MAP = Collections.unmodifiableMap(Arrays.stream(values())
                .collect(Collectors.toMap(CookieItem::getItemName, Function.identity())));
    }

    public static CookieItem from(String itemName) {
        return Optional.ofNullable(COOKIE_MAP.get(itemName))
                .orElseThrow(() -> new NotFoundException(ExceptionStatus.PAYMENT_COOKIE_NOT_FOUND));
    }
}
