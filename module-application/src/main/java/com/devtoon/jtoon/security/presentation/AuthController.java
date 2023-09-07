package com.devtoon.jtoon.security.presentation;

import static com.devtoon.jtoon.security.util.SecurityConstant.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.security.application.AuthService;
import com.devtoon.jtoon.security.request.LogInReq;
import com.devtoon.jtoon.security.response.LoginRes;
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

		response.setHeader(ACCESS_TOKEN_HEADER, BEARER_VALUE + loginRes.accessToken());
		response.setHeader(REFRESH_TOKEN_HEADER, BEARER_VALUE + loginRes.refreshToken());
	}
}
