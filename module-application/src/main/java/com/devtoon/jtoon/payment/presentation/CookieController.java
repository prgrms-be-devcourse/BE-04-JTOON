package com.devtoon.jtoon.payment.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.payment.application.CookieService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cookies")
public class CookieController {

	private CookieService cookieService;

	@GetMapping
	public int getCookieCount() {
		return cookieService.getCookieCount();
	}
}
