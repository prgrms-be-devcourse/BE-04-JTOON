package com.devtoon.jtoon.security.handler;

import static com.devtoon.jtoon.security.util.SecurityConstant.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.devtoon.jtoon.security.application.JwtService;
import com.devtoon.jtoon.security.entity.RefreshToken;
import com.devtoon.jtoon.security.repository.RefreshTokenRepository;
import com.devtoon.jtoon.security.util.TokenCookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtService jwtService;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		Map<String, Object> res = oAuth2User.getAttribute("response");
		String email = (String)res.get("email");
		String accessToken = jwtService.generateAccessToken(email);
		String refreshToken = jwtService.generateRefreshToken();
		response.addCookie(TokenCookie.of(ACCESS_TOKEN_HEADER, accessToken));
		response.addCookie(TokenCookie.of(REFRESH_TOKEN_HEADER, refreshToken));
		refreshTokenRepository.save(RefreshToken.builder()
			.email(email)
			.refreshToken(refreshToken)
			.build()
		);
	}
}
