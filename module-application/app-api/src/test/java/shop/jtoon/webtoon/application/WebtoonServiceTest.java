package shop.jtoon.webtoon.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import shop.jtoon.dto.UploadImageDto;
import shop.jtoon.entity.DayOfWeekWebtoon;
import shop.jtoon.entity.GenreWebtoon;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.Webtoon;
import shop.jtoon.entity.enums.DayOfWeek;
import shop.jtoon.entity.enums.Genre;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.member.application.MemberService;
import shop.jtoon.repository.DayOfWeekWebtoonRepository;
import shop.jtoon.repository.GenreWebtoonRepository;
import shop.jtoon.repository.WebtoonRepository;
import shop.jtoon.repository.WebtoonSearchRepository;
import shop.jtoon.response.WebtoonInfoRes;
import shop.jtoon.response.WebtoonItemRes;
import shop.jtoon.service.S3Service;
import shop.jtoon.type.ErrorStatus;
import shop.jtoon.webtoon.factory.WebtoonFactory;
import shop.jtoon.webtoon.request.CreateWebtoonReq;
import shop.jtoon.webtoon.request.GetWebtoonsReq;

@ExtendWith(MockitoExtension.class)
class WebtoonServiceTest {

	@InjectMocks
	private WebtoonService webtoonService;

	@Mock
	private MemberService memberService;

	@Mock
	private S3Service s3Service;

	@Mock
	private WebtoonRepository webtoonRepository;

	@Mock
	private WebtoonSearchRepository webtoonSearchRepository;

	@Mock
	private DayOfWeekWebtoonRepository dayOfWeekWebtoonRepository;

	@Mock
	private GenreWebtoonRepository genreWebtoonRepository;

	private Member member;

	@BeforeEach
	void init() {
		member = spy(WebtoonFactory.createMember());
		lenient().when(member.getId()).thenReturn(1L);
	}

	@DisplayName("createWebtoon - 웹툰 생성 성공 - Void")
	@Test
	void createWebtoon_success() {
		// Given
		CreateWebtoonReq request = WebtoonFactory.createWebtoonReq();
		MockMultipartFile image = WebtoonFactory.createMultipartFile();
		given(memberService.findById(member.getId())).willReturn(member);
		given(s3Service.uploadImage(any(UploadImageDto.class))).willReturn("thumbnailUrl");

		// When
		webtoonService.createWebtoon(member.getId(), image, request);

		// Then
		verify(webtoonRepository).save(any(Webtoon.class));
		verify(dayOfWeekWebtoonRepository).saveAll(ArgumentMatchers.<DayOfWeekWebtoon>anyList());
		verify(genreWebtoonRepository).saveAll(ArgumentMatchers.<GenreWebtoon>anyList());
	}

	@DisplayName("createWebtoon - 웹툰 생성 실패 시 이미지 삭제 서비스 호출 - InvalidRequestException")
	@Test
	void createWebtoon_fail_invalid_request() {
		// Given
		CreateWebtoonReq request = WebtoonFactory.createWebtoonReq();
		MockMultipartFile image = WebtoonFactory.createMultipartFile();
		Long memberId = member.getId();
		given(memberService.findById(member.getId())).willReturn(member);
		given(s3Service.uploadImage(any(UploadImageDto.class))).willReturn("thumbnailUrl");
		given(webtoonRepository.save(any(Webtoon.class))).willThrow(new RuntimeException());

		// When, Then
		assertThatThrownBy(() -> webtoonService.createWebtoon(memberId, image, request))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessage(ErrorStatus.WEBTOON_CREATE_FAIL.getMessage());
		verify(s3Service).deleteImage("thumbnailUrl");
	}

	@DisplayName("createWebtoon - 웹툰 생성 실패, 제목 중복 - DuplicatedException")
	@Test
	void createWebtoon_fail_duplicate_title() {
		// Given
		CreateWebtoonReq request = WebtoonFactory.createWebtoonReq();
		MockMultipartFile image = WebtoonFactory.createMultipartFile();
		Long memberId = member.getId();
		given(webtoonRepository.existsByTitle(any(String.class))).willReturn(true);

		// When, Then
		assertThatThrownBy(() -> webtoonService.createWebtoon(memberId, image, request))
			.isInstanceOf(DuplicatedException.class)
			.hasMessage(ErrorStatus.WEBTOON_TITLE_DUPLICATED.getMessage());
	}

	@DisplayName("getWebtoons - 웹툰 목록 조회 리스트 없을 때 - Empty Map")
	@Test
	void getWebtoons_empty_map() {
		// Given
		GetWebtoonsReq request = WebtoonFactory.getWebtoonsReq();
		Map<DayOfWeek, List<WebtoonItemRes>> actual = new HashMap<>();
		List<DayOfWeekWebtoon> expect = new ArrayList<>();

		given(webtoonSearchRepository.findWebtoons(any(DayOfWeek.class), any(String.class))).willReturn(expect);

		// When
		actual = webtoonService.getWebtoons(request);

		// Then
		assertThat(actual).isEmpty();
	}

	@DisplayName("getWebtoons - 웹툰 목록 조회 성공 - Map")
	@Test
	void getWebtoons_success() {
		// Given
		GetWebtoonsReq request = WebtoonFactory.getWebtoonsReq();
		Webtoon webtoon = WebtoonFactory.createWebtoon(member);
		Map<DayOfWeek, List<WebtoonItemRes>> actual = new HashMap<>();

		List<DayOfWeekWebtoon> expect = new ArrayList<>();
		expect.add(DayOfWeekWebtoon.create(DayOfWeek.MON, webtoon));
		expect.add(DayOfWeekWebtoon.create(DayOfWeek.MON, webtoon));
		expect.add(DayOfWeekWebtoon.create(DayOfWeek.FRI, webtoon));

		given(webtoonSearchRepository.findWebtoons(any(DayOfWeek.class), any(String.class))).willReturn(expect);

		// When
		actual = webtoonService.getWebtoons(request);

		// Then
		assertThat(actual).hasSize(2);
		assertThat(actual.get(DayOfWeek.MON)).hasSize(2);
		assertThat(actual.get(DayOfWeek.FRI)).hasSize(1);
	}

	@DisplayName("getWebtoon - 웹툰 단건 조회 성공 - WebtoonInfoRes")
	@Test
	void getWebtoon_success() {
		// Given
		Webtoon webtoon = WebtoonFactory.createWebtoon(member);
		List<DayOfWeekWebtoon> dayOfWeekWebtoon = new ArrayList<>();
		List<GenreWebtoon> genres = new ArrayList<>();
		dayOfWeekWebtoon.add(DayOfWeekWebtoon.create(DayOfWeek.MON, webtoon));
		dayOfWeekWebtoon.add(DayOfWeekWebtoon.create(DayOfWeek.FRI, webtoon));
		genres.add(GenreWebtoon.create(Genre.ACTION, webtoon));

		given(webtoonRepository.findById(anyLong())).willReturn(Optional.of(webtoon));
		given(dayOfWeekWebtoonRepository.findByWebtoon(webtoon)).willReturn(dayOfWeekWebtoon);
		given(genreWebtoonRepository.findByWebtoon(webtoon)).willReturn(genres);

		// When
		WebtoonInfoRes actual = webtoonService.getWebtoon(anyLong());

		// Then
		assertThat(actual.thumbnailUrl()).isEqualTo("https://webtoons/thumbnail");
		assertThat(actual.dayOfWeeks()).hasSize(2);
		assertThat(actual.dayOfWeeks().get(0)).isEqualTo(DayOfWeek.MON.toString());
		assertThat(actual.genres()).hasSize(1);
		assertThat(actual.genres().get(0).type()).isEqualTo(Genre.ACTION);
	}

	@DisplayName("getWebtoon - 웹턴 단건 조회 실패, 웹툰 없음 - NotFoundException")
	@Test
	void getWebtoon_fail_notfound_webtoon() {
		// Given
		given(webtoonRepository.findById(anyLong())).willReturn(Optional.empty());

		// When, Then
		assertThatThrownBy(() -> webtoonService.getWebtoon(1L))
			.isInstanceOf(NotFoundException.class)
			.hasMessage(ErrorStatus.WEBTOON_NOT_FOUND.getMessage());
	}
}
