package com.devtoon.jtoon.webtoon.request;

import jakarta.validation.constraints.Min;

public record GetEpisodeReq(
	@Min(1) int no
) {
}
