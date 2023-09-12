package com.devtoon.jtoon.webtoon.service;

import static com.devtoon.jtoon.error.model.ErrorStatus.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.error.exception.NotFoundException;
import com.devtoon.jtoon.global.util.CustomPageRequest;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.entity.MemberCookie;
import com.devtoon.jtoon.member.repository.MemberCookieRepository;
import com.devtoon.jtoon.webtoon.entity.Episode;
import com.devtoon.jtoon.webtoon.entity.PurchasedEpisode;
import com.devtoon.jtoon.webtoon.entity.Webtoon;
import com.devtoon.jtoon.webtoon.repository.EpisodeRepository;
import com.devtoon.jtoon.webtoon.repository.EpisodeSearchRepository;
import com.devtoon.jtoon.webtoon.repository.PurchasedEpisodeRepository;
import com.devtoon.jtoon.webtoon.request.CreateEpisodeReq;
import com.devtoon.jtoon.webtoon.response.EpisodeRes;
import com.devtoon.jtoon.webtoon.response.EpisodesRes;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EpisodeDomainService {

	private final EpisodeRepository episodeRepository;
	private final EpisodeSearchRepository episodeSearchRepository;
	private final PurchasedEpisodeRepository purchasedEpisodeRepository;
	private final MemberCookieRepository memberCookieRepository;

	@Transactional
	public void createEpisode(Webtoon webtoon, String mainUrl, String thumbnailUrl, CreateEpisodeReq request) {
		Episode episode = request.toEntity(webtoon, mainUrl, thumbnailUrl);
		episodeRepository.save(episode);
	}

	public List<EpisodesRes> getEpisodes(Long webtoonId, CustomPageRequest request) {
		return episodeSearchRepository.getEpisodes(webtoonId, request)
			.stream()
			.map(EpisodesRes::from)
			.toList();
	}

	public EpisodeRes getEpisode(Long episodeId) {
		Episode episode = getEpisodeById(episodeId);
		return EpisodeRes.from(episode);
	}

	@Transactional
	public void purchaseEpisode(Member member, Long episodeId) {
		Episode episode = getEpisodeById(episodeId);
		MemberCookie memberCookie = getMemberCookieById(member.getId());
		memberCookie.decreaseCookieCount(episode.getCookieCount());
		purchasedEpisodeRepository.save(PurchasedEpisode.builder()
			.member(member)
			.episode(episode)
			.build()
		);
	}

	private Episode getEpisodeById(Long episodeId) {
		return episodeRepository.findById(episodeId)
			.orElseThrow(() -> new NotFoundException(EPISODE_NOT_FOUND));
	}

	private MemberCookie getMemberCookieById(Long memberId) {
		return memberCookieRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(MEMBER_COOKIE_NOT_FOUND));
	}
}
