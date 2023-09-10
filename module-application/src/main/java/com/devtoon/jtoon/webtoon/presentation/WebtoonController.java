package com.devtoon.jtoon.webtoon.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.security.domain.jwt.MemberThreadLocal;
import com.devtoon.jtoon.webtoon.application.WebtoonService;
import com.devtoon.jtoon.webtoon.request.CreateEpisodeReq;
import com.devtoon.jtoon.webtoon.request.CreateWebtoonReq;
import com.devtoon.jtoon.webtoon.response.WebtoonInfoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webtoons")
public class WebtoonController {

	private final WebtoonService webtoonService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void createWebtoon(@RequestPart MultipartFile thumbnailImage, @RequestPart @Valid CreateWebtoonReq request) {
		Member member = MemberThreadLocal.getMember();
		webtoonService.createWebtoon(member, thumbnailImage, request);
	}

	@GetMapping("/{webtoonId}")
	@ResponseStatus(HttpStatus.CREATED)
	public WebtoonInfoRes getWebtoon(@PathVariable Long webtoonId) {
		return webtoonService.getWebtoon(webtoonId);
	}

	@PostMapping("/{webtoonId}")
	@ResponseStatus(HttpStatus.CREATED)
	public void createEpisode(@PathVariable Long webtoonId, @Valid CreateEpisodeReq req) {
		webtoonService.createEpisode(webtoonId, req);
	}
}
