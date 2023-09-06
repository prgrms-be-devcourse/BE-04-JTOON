package com.devtoon.jtoon.security.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.security.application.AuthService;
import com.devtoon.jtoon.security.request.LogInReq;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public void login(@RequestBody @Valid LogInReq logInReq, HttpServletResponse response) {
		String token = authService.login(logInReq);

		response.setHeader("Set-Cookie", "Bearer " + token);
	}
}
