package shop.jtoon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.jtoon.dto.MemberDto;
import shop.jtoon.entity.CookieItem;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.MemberCookie;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.factory.CreatorFactory;
import shop.jtoon.repository.MemberCookieRepository;
import shop.jtoon.repository.MemberRepository;
import shop.jtoon.type.ErrorStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberCookieDomainServiceTest {

    @InjectMocks
    private MemberCookieDomainService memberCookieDomainService;

    @Mock
    private MemberCookieRepository memberCookieRepository;

    @Mock
    private MemberRepository memberRepository;

    private Member member;
    private MemberDto memberDto;

    @BeforeEach
    void beforeEach() {
        member = CreatorFactory.createMember("example123@naver.com");
        memberDto = MemberDto.toDto(member);
    }

    @DisplayName("createMemberCookie - 한 회원의 쿠키 정보가 성공적으로 저장될 때, - Void")
    @Test
    void createMemberCookie_Void() {
        // Given
        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.of(member));

        // When
        memberCookieDomainService.createMemberCookie(CookieItem.COOKIE_ONE, memberDto);

        // Then
        verify(memberCookieRepository).save(any(MemberCookie.class));
    }

    @DisplayName("createMemberCookie - 해당 이메일에 대한 회원이 존재하지 않을 때, - NotFoundException")
    @Test
    void createMemberCookie_NotFoundException() {
        // Given
        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

        // When, Then
        assertThatThrownBy(() -> memberCookieDomainService.createMemberCookie(CookieItem.COOKIE_ONE, memberDto))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(ErrorStatus.MEMBER_EMAIL_NOT_FOUND.getMessage());
    }

    @DisplayName("useCookie - 해당 회원에 대한 쿠키 정보가 존재하지 않을 때, - NotFoundException (MemberCookie)")
    @Test
    void useCookie_NotFoundException_MemberCookie() {
        // Given
        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.of(member));
        given(memberCookieRepository.findByMember(any(Member.class))).willReturn(Optional.empty());

        // When, Then
        assertThatThrownBy(() -> memberCookieDomainService.useCookie(2, memberDto))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(ErrorStatus.MEMBER_COOKIE_NOT_FOUND.getMessage());
    }

    @DisplayName("useCookie - 해당 이메일에 대한 회원이 존재하지 않을 때, - NotFoundException (Member)")
    @Test
    void useCookie_NotFoundException_Member() {
        // Given
        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

        // When, Then
        assertThatThrownBy(() -> memberCookieDomainService.useCookie(2, memberDto))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(ErrorStatus.MEMBER_EMAIL_NOT_FOUND.getMessage());
    }

    @DisplayName("useCookie - 사용할 쿠키 갯수가 해당 회원이 가진 쿠키 갯수보다 적을 때, - InvalidRequestException")
    @Test
    void useCookie_InvalidRequestException() {
        // Given
        MemberCookie memberCookie = MemberCookie.create(0, member);
        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.of(member));
        given(memberCookieRepository.findByMember(any(Member.class))).willReturn(Optional.of(memberCookie));

        // When, Then
        assertThatThrownBy(() -> memberCookieDomainService.useCookie(2, memberDto))
            .isInstanceOf(InvalidRequestException.class)
            .hasMessage(ErrorStatus.EPISODE_NOT_ENOUGH_COOKIES.getMessage());
    }

    @DisplayName("useCookie - 쿠키 갯수가 충분할 때, - 남은 쿠키 갯수")
    @Test
    void useCookie_CookieCount() {
        // Given
        MemberCookie memberCookie = MemberCookie.create(7, member);
        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.of(member));
        given(memberCookieRepository.findByMember(any(Member.class))).willReturn(Optional.of(memberCookie));

        // When
        int actual = memberCookieDomainService.useCookie(2, memberDto);

        // Then
        assertThat(actual).isEqualTo(5);
    }

    @DisplayName("getMemberCookie - 해당 이메일에 대한 회원이 존재하지 않을 때, - NotFoundException (Member)")
    @Test
    void getMemberCookie_NotFoundException() {
        // Given
        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

        // When, Then
        assertThatThrownBy(() -> memberCookieDomainService.getMemberCookie(memberDto))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(ErrorStatus.MEMBER_EMAIL_NOT_FOUND.getMessage());
    }

    @DisplayName("getMemberCookie - 해당 회원에 대한 쿠키 정보가 존재하지 않을 때, - NotFoundException (MemberCookie)")
    @Test
    void getMemberCookie_NotFoundException_MemberCookie() {
        // Given
        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.of(member));
        given(memberCookieRepository.findByMember(any(Member.class))).willReturn(Optional.empty());

        // When, Then
        assertThatThrownBy(() -> memberCookieDomainService.getMemberCookie(memberDto))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(ErrorStatus.MEMBER_COOKIE_NOT_FOUND.getMessage());
    }

    @DisplayName("getMemberCookie - 해당 회원이 가진 쿠키 갯수를 성공적으로 조회, - 쿠키 갯수")
    @Test
    void getMemberCookie_CookieCount() {
        // Given
        MemberCookie memberCookie = MemberCookie.create(7, member);
        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.of(member));
        given(memberCookieRepository.findByMember(any(Member.class))).willReturn(Optional.of(memberCookie));

        // When
        int actual = memberCookieDomainService.getMemberCookie(memberDto);

        // Then
        assertThat(actual).isEqualTo(7);
    }
}
