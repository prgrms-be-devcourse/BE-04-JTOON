package com.devtoon.jtoon.member.request;

import static com.devtoon.jtoon.global.util.RegExp.*;

import com.devtoon.jtoon.member.entity.Gender;
import com.devtoon.jtoon.member.entity.LoginType;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.entity.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpReq(
	@Pattern(regexp = EMAIL_PATTERN) String email,
	@Pattern(regexp = PASSWORD_PATTERN) String password,
	@NotBlank @Size(max = 10) String name,
	@NotBlank @Size(max = 30) String nickname,
	@NotNull String gender,
	@Pattern(regexp = PHONE_PATTERN) String phone,
	@NotNull String loginType
) {

	public Member toEntity(String encryptedPassword) {
		return Member.builder()
			.email(email)
			.password(encryptedPassword)
			.name(name)
			.nickname(nickname)
			.gender(Gender.from(gender))
			.phone(phone)
			.role(Role.USER)
			.loginType(LoginType.from(loginType))
			.build();
	}
}
