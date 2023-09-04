package com.devtoon.jtoon.webtoon.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.webtoon.application.WebtoonService;
import com.devtoon.jtoon.webtoon.request.CreateWebtoonReq;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webtoons")
public class WebtoonController {

	private final WebtoonService webtoonService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void createWebtoon(@RequestBody @Valid CreateWebtoonReq request) {
		Long memberId = 1L;
		webtoonService.createWebtoon(memberId, request);
	}
}
