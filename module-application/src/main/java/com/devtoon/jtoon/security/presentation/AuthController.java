package com.devtoon.jtoon.security.presentation;

import static com.devtoon.jtoon.security.util.SecurityConstant.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.security.application.AuthService;
import com.devtoon.jtoon.security.request.LogInReq;
import com.devtoon.jtoon.security.response.LoginRes;
import com.devtoon.jtoon.security.util.TokenCookie;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public void login(@RequestBody @Valid LogInReq logInReq, HttpServletResponse response) {
		LoginRes loginRes = authService.login(logInReq);

		Cookie accessCookie = TokenCookie.of(ACCESS_TOKEN_HEADER, loginRes.accessToken());
		Cookie refreshCookie = TokenCookie.of(REFRESH_TOKEN_HEADER, loginRes.refreshToken());

		response.addCookie(accessCookie);
		response.addCookie(refreshCookie);
	}
}
