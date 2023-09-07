package com.devtoon.jtoon.security.response;

import jakarta.validation.constraints.NotBlank;

public record LoginRes(
	@NotBlank String accessToken,
	@NotBlank String refreshToken
) {
}
