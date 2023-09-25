package shop.jtoon.factory;

import shop.jtoon.dto.MemberDto;
import shop.jtoon.entity.Gender;
import shop.jtoon.entity.LoginType;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.Role;

public class MemberFactory {

    private static String defaultEmail = "test@naver.com";

    public static Member createMember() {
        return Member.builder()
                .email(defaultEmail)
                .password("Qwe123!!")
                .name("홍도산")
                .nickname("개발을담다")
                .gender(Gender.MALE)
                .phone("01012331233")
                .role(Role.USER)
                .loginType(LoginType.LOCAL)
                .build();
    }

    public static MemberDto createMemberDto() {
        return MemberDto.builder()
                .id(1L)
                .email(defaultEmail)
                .name("testName")
                .nickname("testNickname")
                .gender(Gender.MALE)
                .phone("01012331233")
                .role(Role.USER)
                .build();
    }

    public static MemberDto createMemberDto(Long id, Member member) {
        return MemberDto.builder()
                .id(id)
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .phone(member.getPhone())
                .role(member.getRole())
                .build();
    }
}
