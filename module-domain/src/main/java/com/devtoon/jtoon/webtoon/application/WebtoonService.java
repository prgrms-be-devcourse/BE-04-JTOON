package com.devtoon.jtoon.webtoon.application;

import static com.devtoon.jtoon.common.ImageType.*;
import static com.devtoon.jtoon.error.model.ErrorStatus.*;
import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devtoon.jtoon.application.S3Service;
import com.devtoon.jtoon.common.FileName;
import com.devtoon.jtoon.error.exception.DuplicatedException;
import com.devtoon.jtoon.error.exception.InvalidRequestException;
import com.devtoon.jtoon.error.exception.NotFoundException;
import com.devtoon.jtoon.global.util.CustomPageRequest;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.repository.MemberRepository;
import com.devtoon.jtoon.webtoon.entity.DayOfWeekWebtoon;
import com.devtoon.jtoon.webtoon.entity.Episode;
import com.devtoon.jtoon.webtoon.entity.GenreWebtoon;
import com.devtoon.jtoon.webtoon.entity.Webtoon;
import com.devtoon.jtoon.webtoon.entity.enums.DayOfWeek;
import com.devtoon.jtoon.webtoon.repository.DayOfWeekWebtoonRepository;
import com.devtoon.jtoon.webtoon.repository.EpisodeRepository;
import com.devtoon.jtoon.webtoon.repository.EpisodeSearchRepository;
import com.devtoon.jtoon.webtoon.repository.GenreWebtoonRepository;
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
public class WebtoonService {

	private final WebtoonRepository webtoonRepository;
	private final WebtoonSearchRepository webtoonSearchRepository;
	private final DayOfWeekWebtoonRepository dayOfWeekWebtoonRepository;
	private final GenreWebtoonRepository genreWebtoonRepository;
	private final EpisodeRepository episodeRepository;
	private final EpisodeSearchRepository episodeSearchRepository;
	private final MemberRepository memberRepository;
	private final S3Service s3Service;

	@Transactional
	public void createWebtoon(Member member, MultipartFile thumbnailImage, CreateWebtoonReq request) {
		validateDuplicateTitle(request.title());
		String thumbnailUrl = s3Service.upload(
			WEBTOON_THUMBNAIL,
			request.title(),
			FileName.forWebtoon(),
			thumbnailImage
		);
		Webtoon webtoon = request.toWebtoonEntity(member, thumbnailUrl);
		List<DayOfWeekWebtoon> dayOfWeekWebtoons = request.toDayOfWeekWebtoonEntity(webtoon);
		List<GenreWebtoon> genreWebtoons = request.toGenreWebtoonEntity(webtoon);
		webtoonRepository.save(webtoon);
		dayOfWeekWebtoonRepository.saveAll(dayOfWeekWebtoons);
		genreWebtoonRepository.saveAll(genreWebtoons);
	}

	@Transactional
	public void createEpisode(
		Member member,
		Long webtoonId,
		CreateEpisodeReq request,
		MultipartFile mainImage,
		MultipartFile thumbnailImage
	) {
		Webtoon webtoon = getWebtoonById(webtoonId);
		validateAuthorOfWebtoon(member, webtoon);
		String mainUrl = s3Service.upload(
			EPISODE_MAIN,
			webtoon.getTitle(),
			FileName.forEpisode(request.no()),
			mainImage
		);
		String thumbnailUrl = s3Service.upload(
			EPISODE_THUMBNAIL,
			webtoon.getTitle(),
			FileName.forEpisode(request.no()),
			thumbnailImage
		);
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

	private Webtoon getWebtoonById(Long webtoonId) {
		return webtoonRepository.findById(webtoonId)
			.orElseThrow(() -> new NotFoundException(WEBTOON_NOT_FOUND));
	}

	private Episode getEpisodeById(Long episodeId) {
		return episodeRepository.findById(episodeId)
			.orElseThrow(() -> new NotFoundException(EPISODE_NOT_FOUND));
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

	private void validateDuplicateTitle(String title) {
		if (webtoonRepository.existsByTitle(title)) {
			throw new DuplicatedException(WEBTOON_TITLE_DUPLICATED);
		}
	}

	private void validateAuthorOfWebtoon(Member member, Webtoon webtoon) {
		if (!webtoon.isAuthor(member.getId())) {
			throw new InvalidRequestException(WEBTOON_NOT_AUTHOR);
		}
	}
}
