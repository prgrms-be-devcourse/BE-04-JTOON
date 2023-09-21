package shop.jtoon.security.request;

import static shop.jtoon.util.RegExp.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginReq(
	@Pattern(regexp = EMAIL_PATTERN) String email,
	@Pattern(regexp = PASSWORD_PATTERN) @NotBlank String password
) {
}
