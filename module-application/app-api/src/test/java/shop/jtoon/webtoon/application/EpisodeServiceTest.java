package shop.jtoon.webtoon.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import shop.jtoon.dto.UploadImageDto;
import shop.jtoon.entity.Episode;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PurchasedEpisode;
import shop.jtoon.entity.Webtoon;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.member.application.MemberService;
import shop.jtoon.payment.application.MemberCookieService;
import shop.jtoon.repository.EpisodeRepository;
import shop.jtoon.repository.EpisodeSearchRepository;
import shop.jtoon.repository.PurchasedEpisodeRepository;
import shop.jtoon.response.EpisodeInfoRes;
import shop.jtoon.response.EpisodeItemRes;
import shop.jtoon.service.S3Service;
import shop.jtoon.webtoon.factory.WebtoonFactory;
import shop.jtoon.webtoon.request.CreateEpisodeReq;
import shop.jtoon.webtoon.request.GetEpisodesReq;

@ExtendWith(MockitoExtension.class)
class EpisodeServiceTest {

	@InjectMocks
	private EpisodeService episodeService;

	@Mock
	private MemberService memberService;

	@Mock
	private MemberCookieService memberCookieService;

	@Mock
	private WebtoonService webtoonService;

	@Mock
	private S3Service s3Service;

	@Mock
	private EpisodeRepository episodeRepository;

	@Mock
	private EpisodeSearchRepository episodeSearchRepository;

	@Mock
	private PurchasedEpisodeRepository purchasedEpisodeRepository;

	private Member member;
	private Webtoon webtoon;

	@BeforeEach
	void beforeEach() {
		member = spy(WebtoonFactory.createMember());
		lenient().when(member.getId()).thenReturn(1L);
		webtoon = spy(WebtoonFactory.createWebtoon(member));
		lenient().when(webtoon.getId()).thenReturn(1L);
	}

	@DisplayName("createEpisode - 회차 생성 성공, - Void")
	@Test
	void createEpisode_Void() {
		// Given
		CreateEpisodeReq request = WebtoonFactory.createEpisodeReq();
		MockMultipartFile image = WebtoonFactory.createMultipartFile();
		given(webtoonService.getWebtoonById(webtoon.getId())).willReturn(webtoon);
		given(s3Service.uploadImage(any(UploadImageDto.class))).willReturn("https://webtoons/episodes/image");

		// When
		episodeService.createEpisode(member.getId(), webtoon.getId(), image, image, request);

		// Then
		verify(episodeRepository).save(any(Episode.class));
	}

	@DisplayName("createEpisode - 회차 번호 중복, - DuplicatedException")
	@Test
	void createEpisode_DuplicatedException() {
		// Given
		CreateEpisodeReq request = WebtoonFactory.createEpisodeReq();
		MockMultipartFile image = WebtoonFactory.createMultipartFile();
		given(webtoonService.getWebtoonById(webtoon.getId())).willReturn(webtoon);
		given(episodeRepository.existsByWebtoonAndNo(any(Webtoon.class), anyInt())).willReturn(true);

		// When, Then
		assertThatThrownBy(() -> episodeService.createEpisode(member.getId(), webtoon.getId(), image, image, request))
			.isInstanceOf(DuplicatedException.class)
			.hasMessage("이미 존재하는 회차 번호입니다.");
	}

	@DisplayName("createEpisode - 회차 생성 실패 시 이미지 삭제 서비스 호출, - InvalidRequestException")
	@Test
	void createEpisode_InvalidRequestException() {
		// Given
		CreateEpisodeReq request = WebtoonFactory.createEpisodeReq();
		MockMultipartFile image = WebtoonFactory.createMultipartFile();
		given(webtoonService.getWebtoonById(webtoon.getId())).willReturn(webtoon);
		given(s3Service.uploadImage(any(UploadImageDto.class))).willReturn("https://webtoons/episodes/image");
		given(episodeRepository.save(any(Episode.class))).willThrow(new RuntimeException());

		// When, Then
		assertThatThrownBy(() -> episodeService.createEpisode(member.getId(), webtoon.getId(), image, image, request))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessage("회차 생성에 실패했습니다.");
		verify(s3Service, times(2)).deleteImage(anyString());
	}

	@DisplayName("getEpisodes - 조회할 회차 리스트가 없을 때, - Empty List")
	@Test
	void getEpisodes_EmptyList() {
		// Given
		GetEpisodesReq request = WebtoonFactory.createGetEpisodesReq();
		List<Episode> episodes = new ArrayList<>();
		given(episodeSearchRepository.getEpisodes(webtoon.getId(), request.getSize(), request.getOffset()))
			.willReturn(episodes);

		// When
		List<EpisodeItemRes> actual = episodeService.getEpisodes(webtoon.getId(), request);

		// Then
		assertThat(actual).isEmpty();
	}

	@DisplayName("getEpisodes - 회차 리스트 조회 성공, - List<EpisodeItemRes>")
	@Test
	void getEpisodes_EpisodeItemResList() {
		// Given
		GetEpisodesReq request = WebtoonFactory.createGetEpisodesReq();
		List<Episode> episodes = new ArrayList<>();
		episodes.add(WebtoonFactory.createEpisode(webtoon, 1));
		episodes.add(WebtoonFactory.createEpisode(webtoon, 2));
		given(episodeSearchRepository.getEpisodes(webtoon.getId(), request.getSize(), request.getOffset()))
			.willReturn(episodes);

		// When
		List<EpisodeItemRes> actual = episodeService.getEpisodes(webtoon.getId(), request);

		// Then
		assertThat(actual).hasSize(episodes.size());
	}

	@DisplayName("getEpisode - 회차 정보가 존재하지 않을 때, - NotFoundException")
	@Test
	void getEpisode_NotFoundException() {
		// Given
		given(episodeRepository.findById(anyLong())).willReturn(Optional.empty());

		// When, Then
		assertThatThrownBy(() -> episodeService.getEpisode(1L))
			.isInstanceOf(NotFoundException.class)
			.hasMessage("존재하지 않는 회차입니다.");
	}

	@DisplayName("getEpisode - 회차 정보 조회 성공, - EpisodeInfoRes")
	@Test
	void getEpisode_EpisodeInfoRes() {
		// Given
		Episode episode = WebtoonFactory.createEpisode(webtoon, 1);
		given(episodeRepository.findById(anyLong())).willReturn(Optional.of(episode));

		// When
		EpisodeInfoRes actual = episodeService.getEpisode(1L);

		// Then
		assertThat(actual.mainUrl()).isEqualTo("https://webtoons/episodes/main");
	}

	@DisplayName("purchaseEpisode - 회차 구매 성공, - Void")
	@Test
	void purchaseEpisode_Void() {
		// Given
		Episode episode = spy(WebtoonFactory.createEpisode(webtoon, 1));
		given(episode.getId()).willReturn(1L);
		given(memberService.findById(member.getId())).willReturn(member);
		given(episodeRepository.findById(anyLong())).willReturn(Optional.of(episode));

		// When
		episodeService.purchaseEpisode(member.getId(), episode.getId());

		// Then
		verify(memberCookieService).useCookie(episode.getCookieCount(), member);
		verify(purchasedEpisodeRepository).save(any(PurchasedEpisode.class));
	}
}
