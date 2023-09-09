package com.devtoon.jtoon.webtoon.presentation;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devtoon.jtoon.global.util.CustomPageRequest;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.security.jwt.domain.MemberThreadLocal;
import com.devtoon.jtoon.webtoon.application.WebtoonService;
import com.devtoon.jtoon.webtoon.entity.enums.DayOfWeek;
import com.devtoon.jtoon.webtoon.request.CreateEpisodeReq;
import com.devtoon.jtoon.webtoon.request.CreateWebtoonReq;
import com.devtoon.jtoon.webtoon.request.GetEpisodeReq;
import com.devtoon.jtoon.webtoon.request.GetWebtoonsReq;
import com.devtoon.jtoon.webtoon.response.EpisodeRes;
import com.devtoon.jtoon.webtoon.response.EpisodesRes;
import com.devtoon.jtoon.webtoon.response.WebtoonInfoRes;
import com.devtoon.jtoon.webtoon.response.WebtoonItemRes;

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

	@PostMapping("/{webtoonId}")
	@ResponseStatus(HttpStatus.CREATED)
	public void createEpisode(
		@PathVariable Long webtoonId,
		@RequestPart @Valid CreateEpisodeReq request,
		@RequestPart MultipartFile mainImage,
		@RequestPart(required = false) MultipartFile thumbnailImage
	) {
		Member member = MemberThreadLocal.getMember();
		webtoonService.createEpisode(member, webtoonId, request, mainImage, thumbnailImage);
	}

	@GetMapping
	public Map<DayOfWeek, List<WebtoonItemRes>> getWebtoons(GetWebtoonsReq request) {
		return webtoonService.getWebtoons(request);
	}

	@GetMapping("/{webtoonId}")
	@ResponseStatus(HttpStatus.CREATED)
	public WebtoonInfoRes getWebtoon(@PathVariable Long webtoonId) {
		return webtoonService.getWebtoon(webtoonId);
	}

	@GetMapping("/list")
	@ResponseStatus(HttpStatus.OK)
	public List<EpisodesRes> getEpisodes(@RequestParam Long webtoonId, CustomPageRequest request) {
		return webtoonService.getEpisodes(webtoonId, request);
	}

	@GetMapping("/detail")
	@ResponseStatus(HttpStatus.OK)
	public EpisodeRes detailEpisode(@RequestParam Long episodeId, @RequestParam GetEpisodeReq request) {
		return webtoonService.getDetailEpisode(episodeId, request);
	}
}
