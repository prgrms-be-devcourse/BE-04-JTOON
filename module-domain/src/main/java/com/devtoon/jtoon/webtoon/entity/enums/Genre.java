package com.devtoon.jtoon.webtoon.entity.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Genre {

	ROMANCE("로맨스"),
	FANTASY("판타지"),
	ACTION("액션"),
	DAILY("일상"),
	THRILL("스릴러"),
	COMIC("개그"),
	HISTORICAL("무협/사극"),
	DRAMA("드라마"),
	SENSIBILITY("감성"),
	SPORTS("스포츠");

	private final String text;
}
