package com.devtoon.jtoon.webtoon.presentation;

import static org.springframework.data.domain.Sort.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.global.common.PageRes;
import com.devtoon.jtoon.webtoon.application.WebtoonService;
import com.devtoon.jtoon.webtoon.entity.Webtoon;
import com.devtoon.jtoon.webtoon.request.CreateEpisodeReq;
import com.devtoon.jtoon.webtoon.request.CreateWebtoonReq;
import com.devtoon.jtoon.webtoon.request.GetWebtoonsReq;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webtoons")
public class WebtoonController {

	private final WebtoonService webtoonService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void createWebtoon(@Valid CreateWebtoonReq request) {
		Long memberId = 1L;
		webtoonService.createWebtoon(memberId, request);
	}

	@PostMapping("/{webtoonId}")
	@ResponseStatus(HttpStatus.CREATED)
	public void createEpisode(@PathVariable Long webtoonId, @Valid CreateEpisodeReq req) {
		webtoonService.createEpisode(webtoonId, req);
	}

	@GetMapping
	public PageRes<Webtoon> getWebtoons(
		GetWebtoonsReq req,
		@PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable
	) {
		return webtoonService.getWebtoons(req, pageable);
	}
}
