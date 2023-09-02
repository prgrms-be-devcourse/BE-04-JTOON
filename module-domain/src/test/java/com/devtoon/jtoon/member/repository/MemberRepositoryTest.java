package com.devtoon.jtoon.member.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.devtoon.jtoon.annotation.JpaHelper;
import com.devtoon.jtoon.exception.MemberException;
import com.devtoon.jtoon.member.entity.Gender;
import com.devtoon.jtoon.member.entity.LoginType;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.entity.Role;

@DisplayName("회원 Repository 테스트")
class MemberRepositoryTest extends JpaHelper {

	@Autowired
	MemberRepository memberRepository;

	@Nested
	@DisplayName("회원 도메인 테스트")
	class MemberCreateTest {

		@Test
		@DisplayName("회원 생성 성공 테스트")
		void create_member_success_test() {
			// given
			Member member = Member.builder()
				.email("abc123@naver.com")
				.password("qwer1234")
				.phone("01012345678")
				.gender(Gender.MALE)
				.nickname("nikuname")
				.name("parksey")
				.loginType(LoginType.LOCAL)
				.role(Role.USER)
				.build();

			// when
			Member savedMember = memberRepository.save(member);

			// then
			assertThat(savedMember.getId()).isNotNull();
		}

		@Test
		@DisplayName("회원 생성 실패 테스트 : 이메일")
		void create_member_failBy_email_test() {
			assertThatThrownBy(() -> Member.builder()
				.password("qwer1234")
				.phone("01012345678")
				.gender(Gender.MALE)
				.nickname("nikuname")
				.name("parksey")
				.loginType(LoginType.LOCAL)
				.role(Role.USER)
				.build()).isInstanceOf(MemberException.class);
		}

		@Test
		@DisplayName("회원 생성 실패 테스트 : 핸드폰")
		void create_member_failBy_phone_test() {
			assertThatThrownBy(() -> Member.builder()
				.email("abc123@naver.com")
				.password("qwer1234")
				.gender(Gender.MALE)
				.nickname("nikuname")
				.name("parksey")
				.loginType(LoginType.LOCAL)
				.role(Role.USER)
				.build()).isInstanceOf(MemberException.class);
		}

		@Test
		@DisplayName("회원 생성 실패 테스트 : 닉네임")
		void create_member_failBy_nickName_test() {
			assertThatThrownBy(() -> Member.builder()
				.email("abc123@naver.com")
				.password("qwer1234")
				.phone("01012345678")
				.gender(Gender.MALE)
				.name("parksey")
				.loginType(LoginType.LOCAL)
				.role(Role.USER)
				.build()).isInstanceOf(MemberException.class);
		}
	}
}
