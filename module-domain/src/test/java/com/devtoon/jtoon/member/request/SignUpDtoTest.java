package com.devtoon.jtoon.member.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SignUpDtoTest {

	@ParameterizedTest
	@ValueSource(strings = {
		"",
		"123@naver.com",
		"123",
		"abc",
		"@naver.com",
		"abc@naver",
		"abc@.com",
		"abc@nav.c.c"
	})
	@DisplayName("회원 생성 실패 테스트 : 이메일")
	void create_memeber_failBy_email_test() {

	}
}
