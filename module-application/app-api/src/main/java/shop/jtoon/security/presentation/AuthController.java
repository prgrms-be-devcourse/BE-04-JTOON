package shop.jtoon.security.presentation;

import static shop.jtoon.util.SecurityConstant.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shop.jtoon.security.application.AuthenticationApplicationService;
import shop.jtoon.security.application.JwtApplicationService;
import shop.jtoon.security.request.LoginReq;
import shop.jtoon.security.service.JwtInternalService;
import shop.jtoon.security.util.TokenCookie;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final JwtApplicationService jwtApplicationService;
	private final JwtInternalService jwtInternalService;
	private final AuthenticationApplicationService authenticationApplicationService;

	@PostMapping("/local-login")
	public void login(@RequestBody @Valid LoginReq loginReq, HttpServletResponse response) {
		authenticationApplicationService.loginMember(loginReq);
		String accessToken = jwtInternalService.generateAccessToken(loginReq.email());
		String refreshToken = jwtInternalService.generateRefreshToken();
		jwtApplicationService.saveRefreshTokenDb(loginReq.email(), refreshToken);

		response.addCookie(TokenCookie.of(ACCESS_TOKEN_HEADER, accessToken));
		response.addCookie(TokenCookie.of(REFRESH_TOKEN_HEADER, refreshToken));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public String healthCheck() {
		return "Health Check Success";
	}
}
