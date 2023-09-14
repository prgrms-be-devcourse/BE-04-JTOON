package shop.jtoon.service;

import static shop.jtoon.type.ErrorStatus.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.CreateEpisodeDto;
import shop.jtoon.entity.Episode;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.MemberCookie;
import shop.jtoon.entity.PurchasedEpisode;
import shop.jtoon.entity.Webtoon;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.repository.EpisodeRepository;
import shop.jtoon.repository.EpisodeSearchRepository;
import shop.jtoon.repository.MemberCookieRepository;
import shop.jtoon.repository.PurchasedEpisodeRepository;
import shop.jtoon.response.EpisodeRes;
import shop.jtoon.response.EpisodesRes;
import shop.jtoon.type.CustomPageRequest;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EpisodeDomainService {

	private final EpisodeRepository episodeRepository;
	private final EpisodeSearchRepository episodeSearchRepository;
	private final PurchasedEpisodeRepository purchasedEpisodeRepository;
	private final MemberCookieRepository memberCookieRepository;

	@Transactional
	public void createEpisode(CreateEpisodeDto dto) {
		Webtoon webtoon = dto.toWebtoonEntity();
		Episode episode = dto.toEpisodeEntity(webtoon);
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
