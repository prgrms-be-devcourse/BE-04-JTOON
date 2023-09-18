package shop.jtoon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.jtoon.dto.MemberDto;
import shop.jtoon.dto.PaymentDto;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PaymentInfo;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.factory.CreatorFactory;
import shop.jtoon.repository.MemberRepository;
import shop.jtoon.repository.PaymentInfoRepository;
import shop.jtoon.repository.PaymentInfoSearchRepository;
import shop.jtoon.type.ErrorStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentInfoDomainServiceTest {

    @InjectMocks
    private PaymentInfoDomainService paymentInfoDomainService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PaymentInfoRepository paymentInfoRepository;

    @Mock
    private PaymentInfoSearchRepository paymentInfoSearchRepository;

    private Member member;
    private MemberDto memberDto;
    private PaymentDto paymentDto;

    @BeforeEach
    void beforeEach() {
        member = CreatorFactory.createMember("example123@naver.com");
        memberDto = MemberDto.toDto(member);
        paymentDto = CreatorFactory.createPaymentDto("imp123", "mer123", member.getEmail());
    }


    @DisplayName("createPaymentInfo - 한 회원의 결제 정보가 성공적으로 저장됨 - Void ")
    @Test
    void createPaymentInfo_Void() {
        // Given
        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.of(member));

        // When
        paymentInfoDomainService.createPaymentInfo(paymentDto, memberDto);

        // Then
        verify(paymentInfoRepository).save(any(PaymentInfo.class));
    }

    @DisplayName("createPaymentInfo - 해당 이메일에 대한 회원이 존재하지 않을 때, - NotFoundException")
    @Test
    void createPaymentInfo_NotFoundException() {
        // Given
        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

        // When, Then
        assertThatThrownBy(() -> paymentInfoDomainService.createPaymentInfo(paymentDto, memberDto))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(ErrorStatus.MEMBER_EMAIL_NOT_FOUND.getMessage());
    }
}
