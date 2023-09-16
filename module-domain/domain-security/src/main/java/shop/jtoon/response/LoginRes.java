package shop.jtoon.response;

import lombok.Builder;

@Builder
public record LoginRes(
	String accessToken,
	String refreshToken
) {
	public static LoginRes of(String accessToken, String refreshToken) {
		return LoginRes.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
