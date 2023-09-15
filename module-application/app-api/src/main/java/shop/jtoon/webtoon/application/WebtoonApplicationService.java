package shop.jtoon.webtoon.application;

import static shop.jtoon.common.ImageType.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.CreateEpisodeDto;
import shop.jtoon.dto.CreateWebtoonDto;
import shop.jtoon.dto.UploadImageDto;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.enums.DayOfWeek;
import shop.jtoon.response.EpisodeRes;
import shop.jtoon.response.EpisodesRes;
import shop.jtoon.response.WebtoonInfoRes;
import shop.jtoon.response.WebtoonItemRes;
import shop.jtoon.response.WebtoonRes;
import shop.jtoon.service.EpisodeDomainService;
import shop.jtoon.service.S3Service;
import shop.jtoon.service.WebtoonDomainService;
import shop.jtoon.type.CustomPageRequest;
import shop.jtoon.webtoon.request.CreateEpisodeReq;
import shop.jtoon.webtoon.request.CreateWebtoonReq;
import shop.jtoon.webtoon.request.GetWebtoonsReq;

@Service
@RequiredArgsConstructor
public class WebtoonApplicationService {

	private final WebtoonDomainService webtoonDomainService;
	private final EpisodeDomainService episodeDomainService;
	private final S3Service s3Service;

	@Transactional
	public void createWebtoon(Member member, MultipartFile thumbnailImage, CreateWebtoonReq request) {
		webtoonDomainService.validateDuplicateTitle(request.title());
		UploadImageDto uploadImageDto = request.toUploadImageDto(WEBTOON_THUMBNAIL, thumbnailImage);
		String thumbnailUrl = s3Service.uploadImage(uploadImageDto);
		CreateWebtoonDto dto = request.toDto(member, thumbnailUrl);
		webtoonDomainService.createWebtoon(dto);
	}

	@Transactional
	public void createEpisode(
		Member member,
		Long webtoonId,
		MultipartFile mainImage,
		MultipartFile thumbnailImage,
		CreateEpisodeReq request
	) {
		WebtoonRes webtoonRes = webtoonDomainService.getWebtoonById(webtoonId);
		webtoonDomainService.validateAuthor(member, webtoonRes.author());
		UploadImageDto uploadMainImageDto = request.toUploadImageDto(EPISODE_MAIN, webtoonRes.title(), mainImage);
		UploadImageDto uploadThumbnailImageDto = request.toUploadImageDto(
			EPISODE_THUMBNAIL,
			webtoonRes.title(),
			thumbnailImage
		);
		String mainUrl = s3Service.uploadImage(uploadMainImageDto);
		String thumbnailUrl = s3Service.uploadImage(uploadThumbnailImageDto);
		CreateEpisodeDto dto = request.toDto(webtoonRes, mainUrl, thumbnailUrl);
		episodeDomainService.createEpisode(dto);
	}

	public Map<DayOfWeek, List<WebtoonItemRes>> getWebtoons(GetWebtoonsReq request) {
		return webtoonDomainService.getWebtoons(request.toDto());
	}

	public WebtoonInfoRes getWebtoon(Long webtoonId) {
		return webtoonDomainService.getWebtoon(webtoonId);
	}

	public List<EpisodesRes> getEpisodes(Long webtoonId, CustomPageRequest request) {
		return episodeDomainService.getEpisodes(webtoonId, request);
	}

	public EpisodeRes getEpisode(Long episodeId) {
		return episodeDomainService.getEpisode(episodeId);
	}

	@Transactional
	public void purchaseEpisode(Member member, Long episodeId) {
		episodeDomainService.purchaseEpisode(member, episodeId);
	}
}
