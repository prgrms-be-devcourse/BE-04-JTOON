package com.devtoon.jtoon.webtoon.service;

import static com.devtoon.jtoon.error.model.ErrorStatus.*;
import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.error.exception.DuplicatedException;
import com.devtoon.jtoon.error.exception.InvalidRequestException;
import com.devtoon.jtoon.error.exception.NotFoundException;
import com.devtoon.jtoon.global.util.CustomPageRequest;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.entity.MemberCookie;
import com.devtoon.jtoon.member.repository.MemberCookieRepository;
import com.devtoon.jtoon.webtoon.entity.DayOfWeekWebtoon;
import com.devtoon.jtoon.webtoon.entity.Episode;
import com.devtoon.jtoon.webtoon.entity.GenreWebtoon;
import com.devtoon.jtoon.webtoon.entity.PurchasedEpisode;
import com.devtoon.jtoon.webtoon.entity.Webtoon;
import com.devtoon.jtoon.webtoon.entity.enums.DayOfWeek;
import com.devtoon.jtoon.webtoon.repository.DayOfWeekWebtoonRepository;
import com.devtoon.jtoon.webtoon.repository.EpisodeRepository;
import com.devtoon.jtoon.webtoon.repository.EpisodeSearchRepository;
import com.devtoon.jtoon.webtoon.repository.GenreWebtoonRepository;
import com.devtoon.jtoon.webtoon.repository.PurchasedEpisodeRepository;
import com.devtoon.jtoon.webtoon.repository.WebtoonRepository;
import com.devtoon.jtoon.webtoon.repository.WebtoonSearchRepository;
import com.devtoon.jtoon.webtoon.request.CreateEpisodeReq;
import com.devtoon.jtoon.webtoon.request.CreateWebtoonReq;
import com.devtoon.jtoon.webtoon.request.GetWebtoonsReq;
import com.devtoon.jtoon.webtoon.response.EpisodeRes;
import com.devtoon.jtoon.webtoon.response.EpisodesRes;
import com.devtoon.jtoon.webtoon.response.GenreRes;
import com.devtoon.jtoon.webtoon.response.WebtoonInfoRes;
import com.devtoon.jtoon.webtoon.response.WebtoonItemRes;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebtoonDomainService {

	private final WebtoonRepository webtoonRepository;
	private final WebtoonSearchRepository webtoonSearchRepository;
	private final DayOfWeekWebtoonRepository dayOfWeekWebtoonRepository;
	private final GenreWebtoonRepository genreWebtoonRepository;
	private final EpisodeRepository episodeRepository;
	private final EpisodeSearchRepository episodeSearchRepository;
	private final PurchasedEpisodeRepository purchasedEpisodeRepository;
	private final MemberCookieRepository memberCookieRepository;

	@Transactional
	public void createWebtoon(Member member, String thumbnailUrl, CreateWebtoonReq request) {
		Webtoon webtoon = request.toWebtoonEntity(member, thumbnailUrl);
		List<DayOfWeekWebtoon> dayOfWeekWebtoons = request.toDayOfWeekWebtoonEntity(webtoon);
		List<GenreWebtoon> genreWebtoons = request.toGenreWebtoonEntity(webtoon);
		webtoonRepository.save(webtoon);
		dayOfWeekWebtoonRepository.saveAll(dayOfWeekWebtoons);
		genreWebtoonRepository.saveAll(genreWebtoons);
	}

	@Transactional
	public void createEpisode(Webtoon webtoon, String mainUrl, String thumbnailUrl, CreateEpisodeReq request) {
		Episode episode = request.toEntity(webtoon, mainUrl, thumbnailUrl);
		episodeRepository.save(episode);
	}

	public Map<DayOfWeek, List<WebtoonItemRes>> getWebtoons(GetWebtoonsReq request) {
		return webtoonSearchRepository.findWebtoons(request.day(), request.keyword())
			.stream()
			.collect(groupingBy(
				DayOfWeekWebtoon::getDayOfWeek,
				mapping(dayOfWeekWebtoon -> WebtoonItemRes.from(dayOfWeekWebtoon.getWebtoon()), toList())
			));
	}

	public WebtoonInfoRes getWebtoon(Long webtoonId) {
		Webtoon webtoon = getWebtoonById(webtoonId);
		List<String> dayOfWeeks = getDayOfWeeks(webtoonId);
		List<GenreRes> genres = getGenres(webtoonId);

		return WebtoonInfoRes.of(webtoon, dayOfWeeks, genres);
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

	public void validateDuplicateTitle(String title) {
		if (webtoonRepository.existsByTitle(title)) {
			throw new DuplicatedException(WEBTOON_TITLE_DUPLICATED);
		}
	}

	public void validateAuthorOfWebtoon(Long memberId, Webtoon webtoon) {
		if (!webtoon.isAuthor(memberId)) {
			throw new InvalidRequestException(WEBTOON_NOT_AUTHOR);
		}
	}

	public Webtoon getWebtoonById(Long webtoonId) {
		return webtoonRepository.findById(webtoonId)
			.orElseThrow(() -> new NotFoundException(WEBTOON_NOT_FOUND));
	}

	private Episode getEpisodeById(Long episodeId) {
		return episodeRepository.findById(episodeId)
			.orElseThrow(() -> new NotFoundException(EPISODE_NOT_FOUND));
	}

	private MemberCookie getMemberCookieById(Long memberId) {
		return memberCookieRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(MEMBER_COOKIE_NOT_FOUND));
	}

	private List<String> getDayOfWeeks(Long webtoonId) {
		return dayOfWeekWebtoonRepository.findById(webtoonId)
			.stream()
			.map(webtoon -> webtoon.getDayOfWeek().name())
			.toList();
	}

	private List<GenreRes> getGenres(Long webtoonId) {
		return genreWebtoonRepository.findById(webtoonId)
			.stream()
			.map(GenreRes::from)
			.toList();
	}
}