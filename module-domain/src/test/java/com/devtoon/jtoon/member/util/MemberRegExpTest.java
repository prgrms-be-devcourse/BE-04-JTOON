package com.devtoon.jtoon.member.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("회원 관련 정규식 표현 테스트")
class MemberRegExpTest {

	@Test
	@DisplayName("이메일 정규식 성공 테스트")
	void email_pattern_match_test() {
		assertThat("abc123@naver.com").matches(MemberRegExp.EMAIL_PATTERN);
	}

	@Test
	@DisplayName("이메일 정규식 실패 테스트")
	void email_pattern_fail_test() {
		assertThat("abc123@naver").doesNotMatch(MemberRegExp.EMAIL_PATTERN);
	}

	@Test
	@DisplayName("핸드폰 번호 정규식 성공 테스트")
	void phone_pattern_match_test() {
		assertThat("01012345678").matches(MemberRegExp.PHONE_PATTERN);
	}

	@Test
	@DisplayName("핸드폰 번호 정규식 실패 테스트")
	void phone_pattern_fail_test() {
		assertThat("07012345678").doesNotMatch(MemberRegExp.PHONE_PATTERN);
	}

	@Test
	@DisplayName("비밀번호 정규식 성공 테스트")
	void password_pattern_match_test() {
		assertThat("Testing193!").matches(MemberRegExp.PASSWORD_PATTERN);
	}

	@Test
	@DisplayName("비밀번호 정규식 실패 테스트")
	void password_pattern_fail_test() {
		assertThat("testing193!").doesNotMatch(MemberRegExp.PASSWORD_PATTERN);
	}
}
