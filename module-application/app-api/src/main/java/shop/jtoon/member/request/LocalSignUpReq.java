package shop.jtoon.member.request;

import static shop.jtoon.util.RegExp.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import shop.jtoon.entity.Gender;
import shop.jtoon.entity.LoginType;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.Role;

public record LocalSignUpReq(
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
