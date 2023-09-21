package shop.jtoon.payment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.jtoon.entity.CookieItem;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.MemberCookie;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.payment.factory.CreatorFactory;
import shop.jtoon.repository.MemberCookieRepository;
import shop.jtoon.type.ErrorStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class MemberCookieServiceTest {

    @InjectMocks
    private MemberCookieService memberCookieService;

    @Mock
    private MemberCookieRepository memberCookieRepository;

    private Member member;

    @BeforeEach
    void beforeEach() {
        member = CreatorFactory.createMember("example123@naver.com");
    }

    @DisplayName("createMemberCookie - 한 회원의 쿠키 정보가 성공적으로 저장될 때, - Void")
    @Test
    void createMemberCookie_Void() {
        // When
        memberCookieService.createMemberCookie(CookieItem.COOKIE_ONE.getItemName(), member);

        // Then
        verify(memberCookieRepository).save(any(MemberCookie.class));
    }

    @DisplayName("useCookie - 해당 회원에 대한 쿠키 정보가 존재하지 않을 때, - NotFoundException (MemberCookie)")
    @Test
    void useCookie_NotFoundException_MemberCookie() {
        // Given
        given(memberCookieRepository.findByMember(any(Member.class))).willReturn(Optional.empty());

        // When, Then
        assertThatThrownBy(() -> memberCookieService.useCookie(2, member))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorStatus.MEMBER_COOKIE_NOT_FOUND.getMessage());
    }

    @DisplayName("useCookie - 사용할 쿠키 갯수가 해당 회원이 가진 쿠키 갯수보다 적을 때, - InvalidRequestException")
    @Test
    void useCookie_InvalidRequestException() {
        // Given
        MemberCookie memberCookie = MemberCookie.create(0, member);
        given(memberCookieRepository.findByMember(any(Member.class))).willReturn(Optional.of(memberCookie));

        // When, Then
        assertThatThrownBy(() -> memberCookieService.useCookie(2, member))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(ErrorStatus.EPISODE_NOT_ENOUGH_COOKIES.getMessage());
    }

    @DisplayName("useCookie - 쿠키 갯수가 충분할 때, - 남은 쿠키 갯수")
    @Test
    void useCookie_CookieCount() {
        // Given
        MemberCookie memberCookie = MemberCookie.create(7, member);
        given(memberCookieRepository.findByMember(any(Member.class))).willReturn(Optional.of(memberCookie));

        // When
        int actual = memberCookieService.useCookie(2, member);

        // Then
        assertThat(actual).isEqualTo(5);
    }

    @DisplayName("getMemberCookieCount - 해당 회원에 대한 쿠키 정보가 존재하지 않을 때, - NotFoundException (MemberCookie)")
    @Test
    void getMemberCookieCount_NotFoundException_MemberCookie() {
        // Given
        given(memberCookieRepository.findByMember(any(Member.class))).willReturn(Optional.empty());

        // When, Then
        assertThatThrownBy(() -> memberCookieService.getMemberCookieCount(member))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorStatus.MEMBER_COOKIE_NOT_FOUND.getMessage());
    }

    @DisplayName("getMemberCookieCount - 해당 회원이 가진 쿠키 갯수를 성공적으로 조회, - 쿠키 갯수")
    @Test
    void getMemberCookieCount_CookieCount() {
        // Given
        MemberCookie memberCookie = MemberCookie.create(7, member);
        given(memberCookieRepository.findByMember(any(Member.class))).willReturn(Optional.of(memberCookie));

        // When
        int actual = memberCookieService.getMemberCookieCount(member);

        // Then
        assertThat(actual).isEqualTo(7);
    }
}
