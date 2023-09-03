package com.devtoon.jtoon.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExceptionCode {

	MEMBER_EMAIL_INVALID_FORMAT("올바른 이메일 형식이 아닙니다."),
	MEMBER_PASSWORD_INVALID_FORMAT("올바른 비밀번호형식이 아닙니다."),
	MEMBER_NAME_INVALID_FORMAT("올바른 이름이 아닙니다."),
	MEMBER_NICKNAME_INVALID_FORMAT("올바른 닉네임이 아닙니다."),
	MEMBER_GENDER_INVALID_FORMAT("올바른 성이 아닙니다."),
	MEMBER_PHONE_INVALID_FORMAT("올바른 전화번호 형식이 아닙니다."),
	MEMBER_ROLE_INVALID_FORMAT("올바른 회원 역할이 아닙니다"),
	MEMBER_LOGIN_TYPE_INVALID_FORMAT("올바른 로그인 타입이 아닙니다."),
	MEMBER_MESSAGE_SEND_FAILED("이메일 인증 메세지 전송 실패")
	;

	private final String message;

	public String getMessage() {
		return message;
	}
}
