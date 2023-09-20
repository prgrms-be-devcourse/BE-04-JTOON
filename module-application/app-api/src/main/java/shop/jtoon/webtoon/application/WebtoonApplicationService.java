package shop.jtoon.webtoon.application;

import static shop.jtoon.common.ImageType.*;
import static shop.jtoon.type.ErrorStatus.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.CreateEpisodeDto;
import shop.jtoon.dto.CreateWebtoonDto;
import shop.jtoon.dto.GetEpisodesDto;
import shop.jtoon.dto.PurchaseEpisodeDto;
import shop.jtoon.dto.UploadImageDto;
import shop.jtoon.entity.enums.DayOfWeek;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.response.EpisodeInfoRes;
import shop.jtoon.response.EpisodeRes;
import shop.jtoon.response.EpisodesRes;
import shop.jtoon.response.WebtoonInfoRes;
import shop.jtoon.response.WebtoonItemRes;
import shop.jtoon.response.WebtoonRes;
import shop.jtoon.service.EpisodeDomainService;
import shop.jtoon.service.MemberCookieDomainService;
import shop.jtoon.service.S3Service;
import shop.jtoon.service.WebtoonDomainService;
import shop.jtoon.webtoon.request.CreateEpisodeReq;
import shop.jtoon.webtoon.request.CreateWebtoonReq;
import shop.jtoon.webtoon.request.GetEpisodesReq;
import shop.jtoon.webtoon.request.GetWebtoonsReq;

@Service
@RequiredArgsConstructor
public class WebtoonApplicationService {

	private final WebtoonDomainService webtoonDomainService;
	private final EpisodeDomainService episodeDomainService;
	private final MemberCookieDomainService memberCookieDomainService;
	private final S3Service s3Service;

	@Transactional
	public void createWebtoon(Long memberId, MultipartFile thumbnailImage, CreateWebtoonReq request) {
		webtoonDomainService.validateDuplicateTitle(request.title());
		UploadImageDto uploadImageDto = request.toUploadImageDto(WEBTOON_THUMBNAIL, thumbnailImage);
		String thumbnailUrl = s3Service.uploadImage(uploadImageDto);

		try {
			CreateWebtoonDto dto = request.toDto(memberId, thumbnailUrl);
			webtoonDomainService.createWebtoon(dto);
		} catch (RuntimeException e) {
			s3Service.deleteImage(thumbnailUrl);
			throw new InvalidRequestException(WEBTOON_CREATE_FAIL);
		}
	}

	@Transactional
	public void createEpisode(
		Long memberId,
		Long webtoonId,
		MultipartFile mainImage,
		MultipartFile thumbnailImage,
		CreateEpisodeReq request
	) {
		WebtoonRes webtoon = webtoonDomainService.getWebtoonById(webtoonId);
		webtoonDomainService.validateAuthor(memberId, webtoon.author());
		UploadImageDto uploadMainImageDto = request.toUploadImageDto(EPISODE_MAIN, webtoon.title(), mainImage);
		UploadImageDto uploadThumbnailImageDto = request.toUploadImageDto(
			EPISODE_THUMBNAIL,
			webtoon.title(),
			thumbnailImage
		);
		String mainUrl = s3Service.uploadImage(uploadMainImageDto);
		String thumbnailUrl = s3Service.uploadImage(uploadThumbnailImageDto);

		try {
			CreateEpisodeDto dto = request.toDto(webtoonId, mainUrl, thumbnailUrl);
			episodeDomainService.createEpisode(dto);
		} catch (RuntimeException e) {
			s3Service.deleteImage(mainUrl);
			s3Service.deleteImage(thumbnailUrl);
			throw new InvalidRequestException(EPISODE_CREATE_FAIL);
		}
	}

	public Map<DayOfWeek, List<WebtoonItemRes>> getWebtoons(GetWebtoonsReq request) {
		return webtoonDomainService.getWebtoons(request.toDto());
	}

	public WebtoonInfoRes getWebtoon(Long webtoonId) {
		return webtoonDomainService.getWebtoon(webtoonId);
	}

	public List<EpisodesRes> getEpisodes(Long webtoonId, GetEpisodesReq request) {
		GetEpisodesDto dto = request.toDto(webtoonId);
		return episodeDomainService.getEpisodes(dto);
	}

	public EpisodeInfoRes getEpisode(Long episodeId) {
		return episodeDomainService.getEpisode(episodeId);
	}

	@Transactional
	public void purchaseEpisode(Long memberId, Long episodeId) {
		EpisodeRes episode = episodeDomainService.getEpisodeById(episodeId);
		memberCookieDomainService.useCookie(memberId, episode.getCookieCount());
		PurchaseEpisodeDto dto = PurchaseEpisodeDto.of(memberId, episodeId);
		episodeDomainService.purchaseEpisode(dto);
	}
}
