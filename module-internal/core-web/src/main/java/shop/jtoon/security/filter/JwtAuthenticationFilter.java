package shop.jtoon.security.filter;

import static shop.jtoon.util.SecurityConstant.*;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.jtoon.security.service.JwtInternalService;
import shop.jtoon.security.util.TokenCookie;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final HandlerExceptionResolver handlerExceptionResolver;
	private final JwtInternalService jwtInternalService;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		try {
			String accessToken = Arrays.stream(request.getCookies())
				.filter(coo -> coo.getName().equals(ACCESS_TOKEN_HEADER))
				.map(Cookie::getValue)
				.findFirst()
				.orElse(null);

			if (accessToken != null && accessToken.startsWith(BEARER_VALUE)) {
				accessToken = accessToken.split(SPLIT_DATA)[1];

				if (!jwtInternalService.isTokenValid(accessToken)) {
					String refreshToken = validateAndGetRefreshToken(request);
					accessToken = regenerateTokens(refreshToken, response);
				}

				authenticate(accessToken);
			}
		} catch (MalformedJwtException | BadCredentialsException e) {
			log.error("Token validation failed", e);
			handlerExceptionResolver.resolveException(request, response, null, e);

			return;
		} catch (NullPointerException e) {
			log.error("Cookie is null");
		} catch (Exception e) {
			log.error("Login Failed", e);
			handlerExceptionResolver.resolveException(request, response, null, e);

			return;
		}

		filterChain.doFilter(request, response);
	}

	private String validateAndGetRefreshToken(HttpServletRequest request) {
		String refreshToken = Arrays.stream(request.getCookies())
			.filter(coo -> coo.getName().equals(REFRESH_TOKEN_HEADER))
			.map(Cookie::getValue)
			.findFirst()
			.orElse(null);
		refreshToken = refreshToken.split(SPLIT_DATA)[1];
		jwtInternalService.isTokenValid(refreshToken);
		jwtInternalService.verifyRefreshTokenDb(refreshToken);

		return refreshToken;
	}

	private String regenerateTokens(String refreshToken, HttpServletResponse response) {
		String newAccessToken = jwtInternalService.reGenerateAccessToken(refreshToken);
		String newRefreshToken = jwtInternalService.generateRefreshToken();
		jwtInternalService.updateRefreshTokenDb(newAccessToken, newRefreshToken);
		Cookie accessCookie = TokenCookie.of(ACCESS_TOKEN_HEADER, newAccessToken);
		Cookie refreshCookie = TokenCookie.of(REFRESH_TOKEN_HEADER, newRefreshToken);
		response.addCookie(accessCookie);
		response.addCookie(refreshCookie);

		return newAccessToken;
	}

	private void authenticate(String accessToken) {
		Authentication auth = jwtInternalService.getAuthentication(accessToken);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
