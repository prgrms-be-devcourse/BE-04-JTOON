package shop.jtoon.webtoon.application;

import static java.util.stream.Collectors.*;
import static shop.jtoon.common.ImageType.*;
import static shop.jtoon.type.ErrorStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.UploadImageDto;
import shop.jtoon.entity.DayOfWeekWebtoon;
import shop.jtoon.entity.GenreWebtoon;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.Webtoon;
import shop.jtoon.entity.enums.DayOfWeek;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.member.application.MemberService;
import shop.jtoon.repository.DayOfWeekWebtoonRepository;
import shop.jtoon.repository.GenreWebtoonRepository;
import shop.jtoon.repository.WebtoonRepository;
import shop.jtoon.repository.WebtoonSearchRepository;
import shop.jtoon.response.GenreRes;
import shop.jtoon.response.WebtoonInfoRes;
import shop.jtoon.response.WebtoonItemRes;
import shop.jtoon.service.S3Service;
import shop.jtoon.webtoon.request.CreateWebtoonReq;
import shop.jtoon.webtoon.request.GetWebtoonsReq;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebtoonService {

	private final MemberService memberService;
	private final S3Service s3Service;
	private final WebtoonRepository webtoonRepository;
	private final WebtoonSearchRepository webtoonSearchRepository;
	private final DayOfWeekWebtoonRepository dayOfWeekWebtoonRepository;
	private final GenreWebtoonRepository genreWebtoonRepository;

	@Transactional
	public void createWebtoon(Long memberId, MultipartFile thumbnailImage, CreateWebtoonReq request) {
		Member member = memberService.findById(memberId);
		validateDuplicateTitle(request.title());
		UploadImageDto uploadImageDto = request.toUploadImageDto(WEBTOON_THUMBNAIL, thumbnailImage);
		String thumbnailUrl = s3Service.uploadImage(uploadImageDto);

		try {
			Webtoon webtoon = request.toWebtoonEntity(member, thumbnailUrl);
			List<DayOfWeekWebtoon> dayOfWeekWebtoons = request.toDayOfWeekWebtoonEntity(webtoon);
			List<GenreWebtoon> genreWebtoons = request.toGenreWebtoonEntity(webtoon);
			webtoonRepository.save(webtoon);
			dayOfWeekWebtoonRepository.saveAll(dayOfWeekWebtoons);
			genreWebtoonRepository.saveAll(genreWebtoons);
		} catch (RuntimeException e) {
			s3Service.deleteImage(thumbnailUrl);
			throw new InvalidRequestException(WEBTOON_CREATE_FAIL);
		}
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
		List<String> dayOfWeeks = getDayOfWeeks(webtoon);
		List<GenreRes> genres = getGenres(webtoon);

		return WebtoonInfoRes.of(webtoon, dayOfWeeks, genres);
	}

	public Webtoon getWebtoonById(Long webtoonId) {
		return webtoonRepository.findById(webtoonId)
			.orElseThrow(() -> new NotFoundException(WEBTOON_NOT_FOUND));
	}

	private List<String> getDayOfWeeks(Webtoon webtoon) {
		return dayOfWeekWebtoonRepository.findByWebtoon(webtoon)
			.stream()
			.map(DayOfWeekWebtoon::getDayOfWeekName)
			.toList();
	}

	private List<GenreRes> getGenres(Webtoon webtoon) {
		return genreWebtoonRepository.findByWebtoon(webtoon)
			.stream()
			.map(GenreRes::from)
			.toList();
	}

	private void validateDuplicateTitle(String title) {
		if (webtoonRepository.existsByTitle(title)) {
			throw new DuplicatedException(WEBTOON_TITLE_DUPLICATED);
		}
	}
}
