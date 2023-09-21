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
import shop.jtoon.security.service.AuthenticationService;
import shop.jtoon.security.service.AuthorizationService;
import shop.jtoon.security.service.JwtService;
import shop.jtoon.security.util.TokenCookie;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

	private final HandlerExceptionResolver handlerExceptionResolver;
	private final AuthorizationService authorizationService;
	private final AuthenticationService authenticationService;
	private final JwtService jwtService;

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

				if (!authorizationService.isTokenValid(accessToken)) {
					String refreshToken = getRefreshToken(request);
					jwtService.validateRefreshTokenRedis(refreshToken);
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

	private String getRefreshToken(HttpServletRequest request) {
		String refreshToken = Arrays.stream(request.getCookies())
			.filter(coo -> coo.getName().equals(REFRESH_TOKEN_HEADER))
			.map(Cookie::getValue)
			.findFirst()
			.orElse(null);
		refreshToken = refreshToken.split(SPLIT_DATA)[1];

		return refreshToken;
	}

	private String regenerateTokens(String refreshToken, HttpServletResponse response) {
		String newAccessToken = jwtService.reGenerateAccessToken(refreshToken);
		String newRefreshToken = jwtService.generateRefreshToken();
		jwtService.updateRefreshTokenDb(newAccessToken, newRefreshToken, refreshToken);
		Cookie accessCookie = TokenCookie.of(ACCESS_TOKEN_HEADER, newAccessToken);
		Cookie refreshCookie = TokenCookie.of(REFRESH_TOKEN_HEADER, newRefreshToken);
		response.addCookie(accessCookie);
		response.addCookie(refreshCookie);

		return newAccessToken;
	}

	private void authenticate(String accessToken) {
		Authentication auth = authenticationService.getAuthentication(accessToken);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
