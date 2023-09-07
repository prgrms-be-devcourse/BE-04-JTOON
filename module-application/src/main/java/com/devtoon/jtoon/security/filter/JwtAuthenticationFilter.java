package com.devtoon.jtoon.security.filter;

import static com.devtoon.jtoon.security.util.SecurityConstant.*;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.devtoon.jtoon.security.jwt.application.JwtProvider;
import com.devtoon.jtoon.security.jwt.domain.CustomUserDetails;
import com.devtoon.jtoon.security.jwt.domain.MemberThreadLocal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final HandlerExceptionResolver handlerExceptionResolver;
	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		@NotNull HttpServletResponse response,
		@NotNull FilterChain filterChain
	) throws ServletException, IOException {
		String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);

		if (accessToken != null && accessToken.startsWith(BEARER_VALUE)) {
			try {
				accessToken = accessToken.split(SPACE)[1];
				if (!jwtProvider.isTokenValid(accessToken)) {
					String refreshToken = validateAndGetRefreshToken(request);
					accessToken = regenerateTokens(refreshToken, response);
				}
				authenticate(accessToken);
			} catch (RuntimeException e) {
				log.error("Token validation failed", e);
				handlerExceptionResolver.resolveException(request, response, null, e);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private String validateAndGetRefreshToken(HttpServletRequest request) {
		String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
		refreshToken = refreshToken.split(SPACE)[1];
		jwtProvider.isTokenValid(refreshToken);
		jwtProvider.verifyRefreshTokenDb(refreshToken);
		return refreshToken;
	}

	private String regenerateTokens(String refreshToken, HttpServletResponse response) {
		String newAccessToken = jwtProvider.reGenerateAccessToken(refreshToken);
		String newRefreshToken = jwtProvider.generateRefreshToken();
		jwtProvider.updateRefreshTokenDb(newAccessToken, newRefreshToken);
		response.setHeader(ACCESS_TOKEN_HEADER, BEARER_VALUE + newAccessToken);
		response.setHeader(REFRESH_TOKEN_HEADER, BEARER_VALUE + newRefreshToken);

		return newAccessToken;
	}

	private void authenticate(String accessToken) {
		Authentication auth = jwtProvider.getAuthentication(accessToken);
		CustomUserDetails customUserDetails = (CustomUserDetails)auth.getPrincipal();
		SecurityContextHolder.getContext().setAuthentication(auth);
		MemberThreadLocal.setMember(customUserDetails.getMember());
	}
}
