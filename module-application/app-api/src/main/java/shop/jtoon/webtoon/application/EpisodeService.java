package shop.jtoon.webtoon.application;

import static shop.jtoon.common.ImageType.*;
import static shop.jtoon.type.ErrorStatus.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.UploadImageDto;
import shop.jtoon.entity.Episode;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PurchasedEpisode;
import shop.jtoon.entity.Webtoon;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.payment.application.MemberCookieService;
import shop.jtoon.repository.EpisodeRepository;
import shop.jtoon.repository.EpisodeSearchRepository;
import shop.jtoon.repository.PurchasedEpisodeRepository;
import shop.jtoon.response.EpisodeInfoRes;
import shop.jtoon.response.EpisodeItemRes;
import shop.jtoon.service.MemberDomainService;
import shop.jtoon.service.S3Service;
import shop.jtoon.webtoon.request.CreateEpisodeReq;
import shop.jtoon.webtoon.request.GetEpisodesReq;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EpisodeService {

	private final MemberDomainService memberService; //TODO MemberService 로 변경
	private final MemberCookieService memberCookieService;
	private final WebtoonService webtoonService;
	private final S3Service s3Service;
	private final EpisodeRepository episodeRepository;
	private final EpisodeSearchRepository episodeSearchRepository;
	private final PurchasedEpisodeRepository purchasedEpisodeRepository;

	@Transactional
	public void createEpisode(
		Long memberId,
		Long webtoonId,
		MultipartFile mainImage,
		MultipartFile thumbnailImage,
		CreateEpisodeReq request
	) {
		Webtoon webtoon = webtoonService.getWebtoonById(webtoonId);
		webtoon.validateAuthor(memberId);
		validateDuplicateNo(webtoon, request.no());
		UploadImageDto uploadMainImageDto = request.toUploadImageDto(EPISODE_MAIN, webtoon.getTitle(), mainImage);
		UploadImageDto uploadThumbnailImageDto = request.toUploadImageDto(
			EPISODE_THUMBNAIL,
			webtoon.getTitle(),
			thumbnailImage
		);
		String mainUrl = s3Service.uploadImage(uploadMainImageDto);
		String thumbnailUrl = s3Service.uploadImage(uploadThumbnailImageDto);

		try {
			Episode episode = request.toEntity(webtoon, mainUrl, thumbnailUrl);
			episodeRepository.save(episode);
		} catch (RuntimeException e) {
			s3Service.deleteImage(mainUrl);
			s3Service.deleteImage(thumbnailUrl);
			throw new InvalidRequestException(EPISODE_CREATE_FAIL);
		}
	}

	public List<EpisodeItemRes> getEpisodes(Long webtoonId, GetEpisodesReq request) {
		return episodeSearchRepository.getEpisodes(webtoonId, request.getSize(), request.getOffset())
			.stream()
			.map(EpisodeItemRes::from)
			.toList();
	}

	public EpisodeInfoRes getEpisode(Long episodeId) {
		Episode episode = getEpisodeById(episodeId);
		return EpisodeInfoRes.from(episode);
	}

	@Transactional
	public void purchaseEpisode(Long memberId, Long episodeId) {
		Member member = memberService.findById(memberId);
		Episode episode = getEpisodeById(episodeId);
		memberCookieService.useCookie(episode.getCookieCount(), member);
		PurchasedEpisode purchasedEpisode = PurchasedEpisode.create(member, episode);
		purchasedEpisodeRepository.save(purchasedEpisode);
	}

	private Episode getEpisodeById(Long episodeId) {
		return episodeRepository.findById(episodeId)
			.orElseThrow(() -> new NotFoundException(EPISODE_NOT_FOUND));
	}

	private void validateDuplicateNo(Webtoon webtoon, int no) {
		if (episodeRepository.existsByWebtoonAndNo(webtoon, no)) {
			throw new DuplicatedException(EPISODE_NUMBER_DUPLICATED);
		}
	}
}
