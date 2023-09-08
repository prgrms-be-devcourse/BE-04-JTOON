package com.devtoon.jtoon.webtoon.response;

import com.devtoon.jtoon.member.entity.Member;

import lombok.Builder;

@Builder
public record AuthorRes(
	Long id,
	String nickname
) {

	public static AuthorRes from(Member author) {
		return AuthorRes.builder()
			.id(author.getId())
			.nickname(author.getNickname())
			.build();
	}
}
