package shop.jtoon.security.handler;

import static shop.jtoon.util.SecurityConstant.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import shop.jtoon.security.service.JwtInternalService;
import shop.jtoon.security.service.RefreshTokenService;
import shop.jtoon.security.util.TokenCookie;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtInternalService jwtInternalService;
	private final RefreshTokenService jwtService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		Map<String, Object> res = oAuth2User.getAttribute("response");
		String email = (String)Objects.requireNonNull(res).get("email");
		String accessToken = jwtInternalService.generateAccessToken(email);
		String refreshToken = jwtInternalService.generateRefreshToken();
		response.addCookie(TokenCookie.of(ACCESS_TOKEN_HEADER, accessToken));
		response.addCookie(TokenCookie.of(REFRESH_TOKEN_HEADER, refreshToken));
		jwtService.saveRefreshToken(refreshToken, email);
	}
}
