package shop.jtoon.service;

import static shop.jtoon.type.ErrorStatus.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.CreateEpisodeDto;
import shop.jtoon.dto.PurchaseEpisodeDto;
import shop.jtoon.entity.Episode;
import shop.jtoon.entity.PurchasedEpisode;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.repository.EpisodeRepository;
import shop.jtoon.repository.EpisodeSearchRepository;
import shop.jtoon.repository.PurchasedEpisodeRepository;
import shop.jtoon.response.EpisodeInfoRes;
import shop.jtoon.response.EpisodeRes;
import shop.jtoon.response.EpisodesRes;
import shop.jtoon.type.CustomPageRequest;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EpisodeDomainService {

	private final EpisodeRepository episodeRepository;
	private final EpisodeSearchRepository episodeSearchRepository;
	private final PurchasedEpisodeRepository purchasedEpisodeRepository;

	@Transactional
	public void createEpisode(CreateEpisodeDto dto) {
		Episode episode = dto.toEntity();
		episodeRepository.save(episode);
	}

	public List<EpisodesRes> getEpisodes(Long webtoonId, CustomPageRequest request) {
		return episodeSearchRepository.getEpisodes(webtoonId, request)
			.stream()
			.map(EpisodesRes::from)
			.toList();
	}

	public EpisodeInfoRes getEpisode(Long episodeId) {
		EpisodeRes episode = getEpisodeById(episodeId);
		return EpisodeInfoRes.from(episode);
	}

	@Transactional
	public void purchaseEpisode(PurchaseEpisodeDto dto) {
		PurchasedEpisode purchasedEpisode = dto.toEntity();
		purchasedEpisodeRepository.save(purchasedEpisode);
	}

	public EpisodeRes getEpisodeById(Long episodeId) {
		return episodeRepository.findById(episodeId)
			.map(EpisodeRes::from)
			.orElseThrow(() -> new NotFoundException(EPISODE_NOT_FOUND));
	}
}
