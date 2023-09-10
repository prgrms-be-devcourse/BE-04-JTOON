package com.devtoon.jtoon.security.filter;

import static com.devtoon.jtoon.security.util.SecurityConstant.*;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.devtoon.jtoon.security.application.JwtService;
import com.devtoon.jtoon.security.domain.jwt.CustomUserDetails;
import com.devtoon.jtoon.security.domain.jwt.MemberThreadLocal;
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
	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		@NotNull HttpServletResponse response,
		@NotNull FilterChain filterChain
	) throws ServletException, IOException {
		String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);

		if (accessToken != null && accessToken.startsWith(BEARER_VALUE)) {
			try {
				accessToken = accessToken.split(SPLIT_DATA)[1];
				if (!jwtService.isTokenValid(accessToken)) {
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
		refreshToken = refreshToken.split(SPLIT_DATA)[1];
		jwtService.isTokenValid(refreshToken);
		jwtService.verifyRefreshTokenDb(refreshToken);

		return refreshToken;
	}

	private String regenerateTokens(String refreshToken, HttpServletResponse response) {
		String newAccessToken = jwtService.reGenerateAccessToken(refreshToken);
		String newRefreshToken = jwtService.generateRefreshToken();
		jwtService.updateRefreshTokenDb(newAccessToken, newRefreshToken);
		response.setHeader(ACCESS_TOKEN_HEADER, BEARER_VALUE + newAccessToken);
		response.setHeader(REFRESH_TOKEN_HEADER, BEARER_VALUE + newRefreshToken);

		return newAccessToken;
	}

	private void authenticate(String accessToken) {
		Authentication auth = jwtService.getAuthentication(accessToken);
		CustomUserDetails customUserDetails = (CustomUserDetails)auth.getPrincipal();
		SecurityContextHolder.getContext().setAuthentication(auth);
		MemberThreadLocal.setMember(customUserDetails.getMember());
	}
}
