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
import shop.jtoon.response.LoginRes;
import shop.jtoon.security.request.LoginReq;
import shop.jtoon.security.service.JwtApplicationService;
import shop.jtoon.security.util.TokenCookie;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final JwtApplicationService jwtApplicationService;

	@PostMapping("/local-login")
	public void login(@RequestBody @Valid LoginReq loginReq, HttpServletResponse response) {
		LoginRes loginRes = jwtApplicationService.getLoginTokens(loginReq);
		response.addCookie(TokenCookie.of(ACCESS_TOKEN_HEADER, loginRes.accessToken()));
		response.addCookie(TokenCookie.of(REFRESH_TOKEN_HEADER, loginRes.refreshToken()));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public String healthCheck() {
		return "Health Check Success";
	}
}
