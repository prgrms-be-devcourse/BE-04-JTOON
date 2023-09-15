package shop.jtoon.webtoon.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import shop.jtoon.dto.CreateEpisodeDto;
import shop.jtoon.response.WebtoonRes;

public record CreateEpisodeReq(
	@Min(1) int no,
	@NotBlank @Size(max = 30) String title,
	boolean hasComment,
	@NotNull LocalDateTime openedAt
) {

	public CreateEpisodeDto toDto(WebtoonRes webtoonRes, String mainUrl, String thumbnailUrl) {
		return CreateEpisodeDto.builder()
			.webtoonRes(webtoonRes)
			.mainUrl(mainUrl)
			.thumbnailUrl(thumbnailUrl)
			.no(no)
			.title(title)
			.hasComment(hasComment)
			.openedAt(openedAt)
			.build();
	}
}
