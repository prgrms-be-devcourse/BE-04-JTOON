package shop.jtoon.dto;

import lombok.Builder;
import shop.jtoon.entity.Gender;
import shop.jtoon.entity.Member;

@Builder
public record MemberDto(
	String email,
	String name,
	String nickname,
	Gender gender,
	String phone
) {
	public static MemberDto from(Member member) {
		return MemberDto.builder()
			.email(member.getEmail())
			.name(member.getName())
			.nickname(member.getNickname())
			.gender(member.getGender())
			.phone(member.getPhone())
			.build();
	}
}
