package com.devtoon.jtoon.webtoon.presentation;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.webtoon.application.WebtoonService;
import com.devtoon.jtoon.webtoon.request.CreateEpisodeReq;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webtoons")
public class WebtoonController {

	private final WebtoonService webtoonService;

	@PostMapping("/{webtoonId}")
	public void createEpisode(@PathVariable Long webtoonId, @Valid CreateEpisodeReq req) {
		webtoonService.createEpisode(webtoonId, req);
	}
}
