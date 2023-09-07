package com.devtoon.jtoon.webtoon.application;

import static com.devtoon.jtoon.common.ImageType.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devtoon.jtoon.application.S3Service;
import com.devtoon.jtoon.common.FileName;
import com.devtoon.jtoon.global.common.PageRes;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.repository.MemberRepository;
import com.devtoon.jtoon.webtoon.entity.Episode;
import com.devtoon.jtoon.webtoon.entity.Webtoon;
import com.devtoon.jtoon.webtoon.repository.EpisodeRepository;
import com.devtoon.jtoon.webtoon.repository.WebtoonRepository;
import com.devtoon.jtoon.webtoon.repository.WebtoonSearchRepository;
import com.devtoon.jtoon.webtoon.request.CreateEpisodeReq;
import com.devtoon.jtoon.webtoon.request.CreateWebtoonReq;
import com.devtoon.jtoon.webtoon.request.GetWebtoonsReq;
import com.devtoon.jtoon.webtoon.response.WebtoonInfoRes;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebtoonService {

	private final WebtoonRepository webtoonRepository;
	private final WebtoonSearchRepository webtoonSearchRepository;
	private final MemberRepository memberRepository;
	private final EpisodeRepository episodeRepository;
	private final S3Service s3Service;

	@Transactional
	public void createWebtoon(Member author, MultipartFile thumbnailImage, CreateWebtoonReq request) {
		validateDuplicateTitle(request.title());
		String thumbnailUrl = s3Service.upload(
			WEBTOON_THUMBNAIL,
			request.title(),
			FileName.forWebtoon(),
			thumbnailImage
		);
		Webtoon webtoon = request.toEntity(author, thumbnailUrl);
		webtoonRepository.save(webtoon);
	}

	public WebtoonInfoRes getWebtoon(Long webtoonId) {
		Webtoon webtoon = getWebtoonById(webtoonId);

		return WebtoonInfoRes.from(webtoon);
	}

	@Transactional
	public void createEpisode(Long webtoonId, CreateEpisodeReq req) {
		Webtoon webtoon = getWebtoonById(webtoonId);
		String mainUrl = s3Service.upload(
			EPISODE_MAIN,
			webtoon.getTitle(),
			FileName.forEpisode(req.no()),
			req.mainImage()
		);
		String thumbnailUrl = s3Service.upload(
			EPISODE_THUMBNAIL,
			webtoon.getTitle(),
			FileName.forEpisode(req.no()),
			req.thumbnailImage()
		);
		Episode episode = req.toEntity(webtoon, mainUrl, thumbnailUrl);
		episodeRepository.save(episode);
	}

	public PageRes<Webtoon> getWebtoons(GetWebtoonsReq req, Pageable pageable) {
		long totalCount = webtoonSearchRepository.countBy(req.day(), req.keyword());
		List<Webtoon> webtoons = webtoonSearchRepository.findWebtoons(req.day(), req.keyword(), pageable);

		return PageRes.from(totalCount, webtoons);
	}

	private Webtoon getWebtoonById(Long webtoonId) {
		return webtoonRepository.findById(webtoonId)
			.orElseThrow(() -> new RuntimeException("존재하는 웹툰이 아닙니다."));
	}

	private Member getMemberById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("존재하는 회원이 아닙니다."));
	}

	private void validateDuplicateTitle(String title) {
		if (webtoonRepository.existsByTitle(title)) {
			throw new RuntimeException("이미 존재하는 웹툰 제목입니다.");
		}
	}
}
