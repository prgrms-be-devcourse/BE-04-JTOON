package com.devtoon.jtoon.common;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ImageType {

	WEBTOON_THUMBNAIL("webtoons/%s/thumbnail/%s"),
	EPISODE_MAIN("webtoons/%s/episodes/main/%s"),
	EPISODE_THUMBNAIL("webtoons/%s/episodes/thumbnail/%s"),
	;

	private final String pathFormat;

	public String getPath(String webtoonTitle, String filename) {
		return String.format(pathFormat, webtoonTitle, filename);
	}
}
