package com.devtoon.jtoon.webtoon.application;

import static com.devtoon.jtoon.common.ImageType.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devtoon.jtoon.application.S3Service;
import com.devtoon.jtoon.common.FileName;
import com.devtoon.jtoon.global.common.MemberThreadLocal;
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
import com.devtoon.jtoon.webtoon.service.EpisodeDomainService;
import com.devtoon.jtoon.webtoon.service.WebtoonDomainService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebtoonApplicationService {

	private final WebtoonDomainService webtoonDomainService;
	private final EpisodeDomainService episodeDomainService;
	private final S3Service s3Service;

	@Transactional
	public void createWebtoon(MultipartFile thumbnailImage, CreateWebtoonReq request) {
		Member member = MemberThreadLocal.getMember();
		webtoonDomainService.validateDuplicateTitle(request.title());
		String thumbnailUrl = s3Service.upload(
			UploadImageReq.of(WEBTOON_THUMBNAIL, request.title(), FileName.forWebtoon(), thumbnailImage)
		);
		webtoonDomainService.createWebtoon(member, thumbnailUrl, request);
	}

	@Transactional
	public void createEpisode(
		Long webtoonId,
		MultipartFile mainImage,
		MultipartFile thumbnailImage,
		CreateEpisodeReq request
	) {
		Member member = MemberThreadLocal.getMember();
		Webtoon webtoon = webtoonDomainService.getWebtoonById(webtoonId);
		webtoon.validateAuthor(member.getId());
		String mainUrl = s3Service.upload(
			UploadImageReq.of(EPISODE_MAIN, webtoon.getTitle(), FileName.forEpisode(request.no()), mainImage)
		);
		String thumbnailUrl = s3Service.upload(
			UploadImageReq.of(EPISODE_THUMBNAIL, webtoon.getTitle(), FileName.forEpisode(request.no()), thumbnailImage)
		);
		episodeDomainService.createEpisode(webtoon, mainUrl, thumbnailUrl, request);
	}

	public Map<DayOfWeek, List<WebtoonItemRes>> getWebtoons(GetWebtoonsReq request) {
		return webtoonDomainService.getWebtoons(request);
	}

	public WebtoonInfoRes getWebtoon(Long webtoonId) {
		return webtoonDomainService.getWebtoon(webtoonId);
	}

	public List<EpisodesRes> getEpisodes(Long webtoonId, CustomPageRequest request) {
		return episodeDomainService.getEpisodes(webtoonId, request);
	}

	public EpisodeRes getEpisode(Long episodeId) {
		return episodeDomainService.getEpisode(episodeId);
	}

	@Transactional
	public void purchaseEpisode(Long episodeId) {
		Member member = MemberThreadLocal.getMember();
		episodeDomainService.purchaseEpisode(member, episodeId);
	}
}
