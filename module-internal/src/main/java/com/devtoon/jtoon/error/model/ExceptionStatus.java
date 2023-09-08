package com.devtoon.jtoon.error.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExceptionStatus {

    MEMBER_EMAIL_INVALID_FORMAT("올바른 이메일 형식이 아닙니다."),
    MEMBER_PASSWORD_INVALID_FORMAT("올바른 비밀번호형식이 아닙니다."),
    MEMBER_NAME_INVALID_FORMAT("올바른 이름이 아닙니다."),
    MEMBER_NICKNAME_INVALID_FORMAT("올바른 닉네임이 아닙니다."),
    MEMBER_GENDER_INVALID_FORMAT("올바른 성이 아닙니다."),
    MEMBER_PHONE_INVALID_FORMAT("올바른 전화번호 형식이 아닙니다."),
    MEMBER_ROLE_INVALID_FORMAT("올바른 회원 역할이 아닙니다"),
    MEMBER_LOGIN_TYPE_INVALID_FORMAT("올바른 로그인 타입이 아닙니다."),
    MEMBER_MESSAGE_SEND_FAILED("이메일 인증 메세지 전송 실패"),
    MEMBER_EMAIL_CONFLICT("Email 중복"),

    PAYMENT_IMP_UID_IS_NULL("결제 고유번호 값이 NULL 입니다."),
    PAYMENT_MERCHANT_UID_IS_NULL("주문번호 값이 NULL 입니다."),
    PAYMENT_PAY_METHOD_IS_NULL("결제 방법 값이 NULL 입니다."),
    PAYMENT_COOKIE_ITEM_IS_NULL("쿠키 아이템 값이 NULL 입니다."),
    PAYMENT_AMOUNT_IS_NULL("결제 금액 값이 NULL 입니다."),
    PAYMENT_MEMBER_IS_NULL("결제 사용자 값이 NULL 입니다."),
    PAYMENT_IMP_UID_DUPLICATED("결제 고유번호가 중복되었습니다."),
    PAYMENT_MERCHANT_UID_DUPLICATED("주문 번호가 중복되었습니다."),
    PAYMENT_AMOUNT_INVALID("잘못된 결제 금액입니다."),
    PAYMENT_COOKIE_NOT_FOUND("등록되지 않은 쿠키 상품입니다."),
    ;

    private final String message;
}
