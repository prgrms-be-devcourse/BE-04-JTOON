package com.devtoon.jtoon.security.presentation;

import static com.devtoon.jtoon.security.util.SecurityConstant.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.security.application.AuthService;
import com.devtoon.jtoon.security.request.LogInReq;
import com.devtoon.jtoon.security.response.LoginRes;
import com.devtoon.jtoon.security.util.TokenCookie;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/local-login")
	public void login(@RequestBody @Valid LogInReq logInReq, HttpServletResponse response) {
		LoginRes loginRes = authService.login(logInReq);

		response.addCookie(TokenCookie.of(ACCESS_TOKEN_HEADER, loginRes.accessToken()));
		response.addCookie(TokenCookie.of(REFRESH_TOKEN_HEADER, loginRes.refreshToken()));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public String healthCheck() {
		return "Health Check Success";
	}
}
