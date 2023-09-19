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
import shop.jtoon.security.util.TokenCookie;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final JwtApplicationService jwtApplicationService;
	private final AuthenticationApplicationService authenticationApplicationService;

	@PostMapping("/local-login")
	public void login(@RequestBody @Valid LoginReq loginReq, HttpServletResponse response) {
		String[] tokens = authenticationApplicationService.loginMember(loginReq);
		jwtApplicationService.saveRefreshTokenDb(loginReq.email(), tokens[REFRESH_TOKEN_INDEX]);

		response.addCookie(TokenCookie.of(ACCESS_TOKEN_HEADER, tokens[ACCESS_TOKEN_INDEX]));
		response.addCookie(TokenCookie.of(REFRESH_TOKEN_HEADER, tokens[REFRESH_TOKEN_INDEX]));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public String healthCheck() {
		return "Health Check Success";
	}
}
