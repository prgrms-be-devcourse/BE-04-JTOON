package shop.jtoon.service;

import static java.util.stream.Collectors.*;
import static shop.jtoon.type.ErrorStatus.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.CreateWebtoonDto;
import shop.jtoon.dto.GetWebtoonsDto;
import shop.jtoon.entity.DayOfWeekWebtoon;
import shop.jtoon.entity.GenreWebtoon;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.Webtoon;
import shop.jtoon.entity.enums.DayOfWeek;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.repository.DayOfWeekWebtoonRepository;
import shop.jtoon.repository.GenreWebtoonRepository;
import shop.jtoon.repository.WebtoonRepository;
import shop.jtoon.repository.WebtoonSearchRepository;
import shop.jtoon.response.GenreRes;
import shop.jtoon.response.WebtoonInfoRes;
import shop.jtoon.response.WebtoonItemRes;
import shop.jtoon.response.WebtoonRes;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebtoonDomainService {

	private final WebtoonRepository webtoonRepository;
	private final WebtoonSearchRepository webtoonSearchRepository;
	private final DayOfWeekWebtoonRepository dayOfWeekWebtoonRepository;
	private final GenreWebtoonRepository genreWebtoonRepository;

	@Transactional
	public void createWebtoon(CreateWebtoonDto dto) {
		Webtoon webtoon = dto.toWebtoonEntity();
		List<DayOfWeekWebtoon> dayOfWeekWebtoons = dto.toDayOfWeekWebtoonEntity(webtoon);
		List<GenreWebtoon> genreWebtoons = dto.toGenreWebtoonEntity(webtoon);
		webtoonRepository.save(webtoon);
		dayOfWeekWebtoonRepository.saveAll(dayOfWeekWebtoons);
		genreWebtoonRepository.saveAll(genreWebtoons);
	}

	public Map<DayOfWeek, List<WebtoonItemRes>> getWebtoons(GetWebtoonsDto dto) {
		return webtoonSearchRepository.findWebtoons(dto.day(), dto.keyword())
			.stream()
			.collect(groupingBy(
				DayOfWeekWebtoon::getDayOfWeek,
				mapping(dayOfWeekWebtoon -> WebtoonItemRes.from(dayOfWeekWebtoon.getWebtoon()), toList())
			));
	}

	public WebtoonInfoRes getWebtoon(Long webtoonId) {
		WebtoonRes webtoon = getWebtoonById(webtoonId);
		List<String> dayOfWeeks = getDayOfWeeks(webtoonId);
		List<GenreRes> genres = getGenres(webtoonId);

		return WebtoonInfoRes.of(webtoon, dayOfWeeks, genres);
	}

	public void validateDuplicateTitle(String title) {
		if (webtoonRepository.existsByTitle(title)) {
			throw new DuplicatedException(WEBTOON_TITLE_DUPLICATED);
		}
	}

	public void validateAuthor(Long memberId, Member author) {
		if (!author.isEqual(memberId)) {
			throw new InvalidRequestException(WEBTOON_NOT_AUTHOR);
		}
	}

	public WebtoonRes getWebtoonById(Long webtoonId) {
		return webtoonRepository.findById(webtoonId)
			.map(WebtoonRes::from)
			.orElseThrow(() -> new NotFoundException(WEBTOON_NOT_FOUND));
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
