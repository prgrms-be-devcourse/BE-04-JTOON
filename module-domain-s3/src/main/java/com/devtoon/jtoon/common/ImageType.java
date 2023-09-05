package com.devtoon.jtoon.common;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ImageType {

	WEBTOON_THUMBNAIL("webtoons/thumbnail/%s"),
	EPISODE_MAIN("webtoons/episodes/main/%s"),
	EPISODE_THUMBNAIL("webtoons/episodes/thumbnail/%s"),
	;

	private final String pathFormat;

	public String getPath(String filename) {
		return String.format(pathFormat, filename);
	}
}
