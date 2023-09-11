package com.devtoon.jtoon.security.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRes(
	@NotBlank String accessToken,
	@NotBlank String refreshToken
) {
	public static LoginRes toDto(String accessToken, String refreshToken) {
		return LoginRes.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
