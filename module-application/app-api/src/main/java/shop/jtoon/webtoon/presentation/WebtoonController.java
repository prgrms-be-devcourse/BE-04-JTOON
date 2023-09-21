package shop.jtoon.webtoon.presentation;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shop.jtoon.annotation.CurrentUser;
import shop.jtoon.dto.MemberDto;
import shop.jtoon.entity.enums.DayOfWeek;
import shop.jtoon.response.EpisodeInfoRes;
import shop.jtoon.response.EpisodeItemRes;
import shop.jtoon.response.WebtoonInfoRes;
import shop.jtoon.response.WebtoonItemRes;
import shop.jtoon.webtoon.application.EpisodeService;
import shop.jtoon.webtoon.application.WebtoonService;
import shop.jtoon.webtoon.request.CreateEpisodeReq;
import shop.jtoon.webtoon.request.CreateWebtoonReq;
import shop.jtoon.webtoon.request.GetEpisodesReq;
import shop.jtoon.webtoon.request.GetWebtoonsReq;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webtoons")
public class WebtoonController {

	private final WebtoonService webtoonService;
	private final EpisodeService episodeService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void createWebtoon(
		@CurrentUser MemberDto member,
		@RequestPart MultipartFile thumbnailImage,
		@RequestPart @Valid CreateWebtoonReq request
	) {
		webtoonService.createWebtoon(member.id(), thumbnailImage, request);
	}

	@PostMapping("/{webtoonId}")
	@ResponseStatus(HttpStatus.CREATED)
	public void createEpisode(
		@CurrentUser MemberDto member,
		@PathVariable Long webtoonId,
		@RequestPart MultipartFile mainImage,
		@RequestPart MultipartFile thumbnailImage,
		@RequestPart @Valid CreateEpisodeReq request
	) {
		episodeService.createEpisode(member.id(), webtoonId, mainImage, thumbnailImage, request);
	}

	@GetMapping
	public Map<DayOfWeek, List<WebtoonItemRes>> getWebtoons(GetWebtoonsReq request) {
		return webtoonService.getWebtoons(request);
	}

	@GetMapping("/{webtoonId}")
	@ResponseStatus(HttpStatus.CREATED)
	public WebtoonInfoRes getWebtoon(@PathVariable Long webtoonId) {
		return webtoonService.getWebtoon(webtoonId);
	}

	@GetMapping("/{webtoonId}/episodes")
	@ResponseStatus(HttpStatus.OK)
	public List<EpisodeItemRes> getEpisodes(@PathVariable Long webtoonId, GetEpisodesReq request) {
		return episodeService.getEpisodes(webtoonId, request);
	}

	@GetMapping("/episodes/{episodeId}")
	public EpisodeInfoRes getEpisode(@PathVariable Long episodeId) {
		return episodeService.getEpisode(episodeId);
	}

	@PostMapping("/episodes/{episodeId}/purchase")
	public void purchaseEpisode(@CurrentUser MemberDto member, @PathVariable Long episodeId) {
		episodeService.purchaseEpisode(member.id(), episodeId);
	}
}
