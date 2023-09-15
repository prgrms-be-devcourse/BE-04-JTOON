package shop.jtoon.response;

import lombok.Builder;
import shop.jtoon.entity.Member;

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
