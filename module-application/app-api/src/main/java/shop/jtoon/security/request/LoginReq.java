package shop.jtoon.security.request;

import static shop.jtoon.util.RegExp.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import shop.jtoon.dto.LoginDto;

public record LoginReq(
	@Pattern(regexp = EMAIL_PATTERN) String email,
	@Pattern(regexp = PASSWORD_PATTERN) @NotBlank String password
) {
	public LoginDto toDto() {
		return LoginDto.builder()
			.email(this.email)
			.password(this.password)
			.build();
	}
}
