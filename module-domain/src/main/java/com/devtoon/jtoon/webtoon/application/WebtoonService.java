package com.devtoon.jtoon.webtoon.application;

import static com.devtoon.jtoon.common.ImageType.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devtoon.jtoon.application.S3Service;
import com.devtoon.jtoon.common.FileName;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.webtoon.entity.DayOfWeekWebtoon;
import com.devtoon.jtoon.webtoon.entity.Episode;
import com.devtoon.jtoon.webtoon.entity.GenreWebtoon;
import com.devtoon.jtoon.webtoon.entity.Webtoon;
import com.devtoon.jtoon.webtoon.repository.DayOfWeekWebtoonRepository;
import com.devtoon.jtoon.webtoon.repository.EpisodeRepository;
import com.devtoon.jtoon.webtoon.repository.GenreWebtoonRepository;
import com.devtoon.jtoon.webtoon.repository.WebtoonRepository;
import com.devtoon.jtoon.webtoon.request.CreateEpisodeReq;
import com.devtoon.jtoon.webtoon.request.CreateWebtoonReq;
import com.devtoon.jtoon.webtoon.response.GenreRes;
import com.devtoon.jtoon.webtoon.response.WebtoonInfoRes;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebtoonService {

	private final WebtoonRepository webtoonRepository;
	private final EpisodeRepository episodeRepository;
	private final DayOfWeekWebtoonRepository dayOfWeekWebtoonRepository;
	private final GenreWebtoonRepository genreWebtoonRepository;
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
		Webtoon webtoon = request.toWebtoonEntity(author, thumbnailUrl);
		List<DayOfWeekWebtoon> dayOfWeekWebtoons = request.toDayOfWeekWebtoonEntity(webtoon);
		List<GenreWebtoon> genreWebtoons = request.toGenreWebtoonEntity(webtoon);
		webtoonRepository.save(webtoon);
		dayOfWeekWebtoonRepository.saveAll(dayOfWeekWebtoons);
		genreWebtoonRepository.saveAll(genreWebtoons);
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

	public WebtoonInfoRes getWebtoon(Long webtoonId) {
		Webtoon webtoon = getWebtoonById(webtoonId);
		List<String> dayOfWeeks = getDayOfWeeks(webtoonId);
		List<GenreRes> genres = getGenres(webtoonId);

		return WebtoonInfoRes.of(webtoon, dayOfWeeks, genres);
	}

	private Webtoon getWebtoonById(Long webtoonId) {
		return webtoonRepository.findById(webtoonId)
			.orElseThrow(() -> new RuntimeException("존재하는 웹툰이 아닙니다."));
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
			throw new RuntimeException("이미 존재하는 웹툰 제목입니다.");
		}
	}
}
