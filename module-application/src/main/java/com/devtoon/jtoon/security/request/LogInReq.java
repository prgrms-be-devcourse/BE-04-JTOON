package com.devtoon.jtoon.security.request;

import static com.devtoon.jtoon.global.util.RegExp.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LogInReq(
	@Pattern(regexp = EMAIL_PATTERN) String email,
	@NotBlank String password
) {

}
