package shop.jtoon.dto;

import lombok.Builder;
import shop.jtoon.entity.Gender;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.Role;

@Builder
public record MemberDto(
	String email,
	String name,
	String nickname,
	Gender gender,
	Role role,
	String phone
) {
	public static MemberDto toDto(Member member) {
		return MemberDto.builder()
			.email(member.getEmail())
			.name(member.getName())
			.nickname(member.getNickname())
			.gender(member.getGender())
			.role(member.getRole())
			.phone(member.getPhone())
			.build();
	}
}
