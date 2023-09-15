package shop.jtoon.member.request;

import static shop.jtoon.util.RegExp.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import shop.jtoon.dto.SignUpDto;

public record SignUpReq(
	@Pattern(regexp = EMAIL_PATTERN) String email,
	@Pattern(regexp = PASSWORD_PATTERN) String password,
	@NotBlank @Size(max = 10) String name,
	@NotBlank @Size(max = 30) String nickname,
	@NotNull String gender,
	@Pattern(regexp = PHONE_PATTERN) String phone,
	@NotNull String loginType
) {
	public SignUpDto toDto() {
		return SignUpDto.builder()
			.email(this.email)
			.password(this.password)
			.name(this.name)
			.nickname(this.nickname)
			.gender(this.gender)
			.phone(this.phone)
			.loginType(this.loginType)
			.build();
	}
}
