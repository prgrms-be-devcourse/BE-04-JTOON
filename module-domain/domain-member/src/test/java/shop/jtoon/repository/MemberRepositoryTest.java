package shop.jtoon.repository;

import static org.assertj.core.api.Assertions.*;
import static shop.jtoon.entity.Gender.*;
import static shop.jtoon.entity.LoginType.*;
import static shop.jtoon.entity.Role.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import shop.jtoon.config.JpaConfig;
import shop.jtoon.entity.Gender;
import shop.jtoon.entity.LoginType;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.Role;

@DataJpaTest
@Import(JpaConfig.class)
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	Member member;

	@BeforeEach
	void initMember() {
		member = Member.builder()
			.email("abc@naver.com")
			.password("Testing!123")
			.name("HI")
			.nickname("Unique")
			.gender(MALE)
			.phone("01012345678")
			.role(USER)
			.loginType(LOCAL)
			.build();
	}

	@Test
	@DisplayName("MemberRepository 빈 등록 성공")
	void MemberRepository_bean_NotNull() {
		assertThat(memberRepository).isNotNull();
	}

	@Test
	@DisplayName("멤버 저장 성공")
	void MemberRepository_save_success() {
		// given, when
		Member saved = memberRepository.save(member);

		// then
		assertThat(saved.getId()).isNotNull();
	}

	@Nested
	@DisplayName("멤버 엔티티 생성 실패 테스트")
	class MemberEntityTest {

		String email = "abc@gmail.com";
		String password = "testing!123";
		String name = "HI";
		String nickname = "Unique";
		Gender gender = MALE;
		String phone = "01012345678";
		Role role = USER;
		LoginType loginType = LOCAL;

		@Test
		@DisplayName("멤버 엔티티 이메일 null 테스트")
		void Member_email_null_fail() {
			// given, when, then
			assertThatThrownBy(() -> Member.builder()
				.email(null)
				.password(password)
				.name(name)
				.nickname(nickname)
				.gender(gender)
				.phone(phone)
				.role(role)
				.loginType(loginType)
				.build()).isInstanceOf(NullPointerException.class);
		}

		@Test
		@DisplayName("멤버 엔티티 비밀번호 null 테스트")
		void Member_password_null_fail() {
			// given, when, then
			assertThatThrownBy(() -> Member.builder()
				.email(email)
				.password(null)
				.name(name)
				.nickname(nickname)
				.gender(gender)
				.phone(phone)
				.role(role)
				.loginType(loginType)
				.build()).isInstanceOf(NullPointerException.class);
		}

		@Test
		@DisplayName("멤버 엔티티 이름 null 테스트")
		void Member_name_null_fail() {
			// given, when, then
			assertThatThrownBy(() -> Member.builder()
				.email(email)
				.password(password)
				.name(null)
				.nickname(nickname)
				.gender(gender)
				.phone(phone)
				.role(role)
				.loginType(loginType)
				.build()).isInstanceOf(NullPointerException.class);
		}

		@Test
		@DisplayName("멤버 엔티티 닉네임 null 테스트")
		void Member_nickname_null_fail() {
			// given, when, then
			assertThatThrownBy(() -> Member.builder()
				.email(email)
				.password(password)
				.name(name)
				.nickname(null)
				.gender(gender)
				.phone(phone)
				.role(role)
				.loginType(loginType)
				.build()).isInstanceOf(NullPointerException.class);
		}

		@Test
		@DisplayName("멤버 엔티티 성별 null 테스트")
		void Member_gender_null_fail() {
			// given, when, then
			assertThatThrownBy(() -> Member.builder()
				.email(email)
				.password(password)
				.name(name)
				.nickname(nickname)
				.gender(null)
				.phone(phone)
				.role(role)
				.loginType(loginType)
				.build()).isInstanceOf(NullPointerException.class);
		}

		@Test
		@DisplayName("멤버 엔티티 전화번호 null 테스트")
		void Member_phone_null_fail() {
			// given, when, then
			assertThatThrownBy(() -> Member.builder()
				.email(email)
				.password(password)
				.name(name)
				.nickname(nickname)
				.gender(gender)
				.phone(null)
				.role(role)
				.loginType(loginType)
				.build()).isInstanceOf(NullPointerException.class);
		}
	}
}
