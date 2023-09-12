package com.devtoon.jtoon.webtoon.application;

import static com.devtoon.jtoon.common.ImageType.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devtoon.jtoon.application.S3Service;
import com.devtoon.jtoon.common.FileName;
import com.devtoon.jtoon.global.util.CustomPageRequest;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.request.UploadImageReq;
import com.devtoon.jtoon.webtoon.entity.Webtoon;
import com.devtoon.jtoon.webtoon.entity.enums.DayOfWeek;
import com.devtoon.jtoon.webtoon.request.CreateEpisodeReq;
import com.devtoon.jtoon.webtoon.request.CreateWebtoonReq;
import com.devtoon.jtoon.webtoon.request.GetWebtoonsReq;
import com.devtoon.jtoon.webtoon.response.EpisodeRes;
import com.devtoon.jtoon.webtoon.response.EpisodesRes;
import com.devtoon.jtoon.webtoon.response.WebtoonInfoRes;
import com.devtoon.jtoon.webtoon.response.WebtoonItemRes;
import com.devtoon.jtoon.webtoon.service.WebtoonDomainService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebtoonApplicationService {

	private final WebtoonDomainService webtoonDomainService;
	private final S3Service s3Service;

	@Transactional
	public void createWebtoon(Member member, MultipartFile thumbnailImage, CreateWebtoonReq request) {
		webtoonDomainService.validateDuplicateTitle(request.title());
		String thumbnailUrl = s3Service.upload(
			UploadImageReq.of(WEBTOON_THUMBNAIL, request.title(), FileName.forWebtoon(), thumbnailImage)
		);
		webtoonDomainService.createWebtoon(member, thumbnailUrl, request);
	}

	@Transactional
	public void createEpisode(
		Member member,
		Long webtoonId,
		MultipartFile mainImage,
		MultipartFile thumbnailImage,
		CreateEpisodeReq request
	) {
		Webtoon webtoon = webtoonDomainService.getWebtoonById(webtoonId);
		webtoon.validateAuthor(member.getId());
		String mainUrl = s3Service.upload(
			UploadImageReq.of(EPISODE_MAIN, webtoon.getTitle(), FileName.forEpisode(request.no()), mainImage)
		);
		String thumbnailUrl = s3Service.upload(
			UploadImageReq.of(EPISODE_THUMBNAIL, webtoon.getTitle(), FileName.forEpisode(request.no()), thumbnailImage)
		);
		webtoonDomainService.createEpisode(webtoon, mainUrl, thumbnailUrl, request);
	}

	public Map<DayOfWeek, List<WebtoonItemRes>> getWebtoons(GetWebtoonsReq request) {
		return webtoonDomainService.getWebtoons(request);
	}

	public WebtoonInfoRes getWebtoon(Long webtoonId) {
		return webtoonDomainService.getWebtoon(webtoonId);
	}

	public List<EpisodesRes> getEpisodes(Long webtoonId, CustomPageRequest request) {
		return webtoonDomainService.getEpisodes(webtoonId, request);
	}

	public EpisodeRes getEpisode(Long episodeId) {
		return webtoonDomainService.getEpisode(episodeId);
	}

	@Transactional
	public void purchaseEpisode(Member member, Long episodeId) {
		webtoonDomainService.purchaseEpisode(member, episodeId);
	}
}
