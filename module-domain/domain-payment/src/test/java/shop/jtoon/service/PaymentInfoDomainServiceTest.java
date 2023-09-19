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
import shop.jtoon.dto.PaymentInfoRes;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PaymentInfo;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.factory.CreatorFactory;
import shop.jtoon.repository.MemberRepository;
import shop.jtoon.repository.PaymentInfoRepository;
import shop.jtoon.repository.PaymentInfoSearchRepository;
import shop.jtoon.type.ErrorStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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
    private PaymentDto invalidAmountPaymentDto;

    @BeforeEach
    void beforeEach() {
        member = CreatorFactory.createMember("example123@naver.com");
        memberDto = MemberDto.toDto(member);
        paymentDto = CreatorFactory.createPaymentDto("imp123", "mer123", member.getEmail());
        invalidAmountPaymentDto = CreatorFactory
            .createInvalidAmountPaymentDto("imp789", "mer789", member.getEmail());
    }

    @DisplayName("createPaymentInfo - 한 회원의 결제 정보가 성공적으로 저장될 때, - Void")
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

    @DisplayName("validatePaymentInfo - 결제 정보에 대한 검증이 성공적으로 끝났을 때, - Void")
    @Test
    void validatePaymentInfo_Void() {
        // Given
        given(paymentInfoRepository.existsByImpUid(any(String.class))).willReturn(false);
        given(paymentInfoRepository.existsByMerchantUid(any(String.class))).willReturn(false);

        // When
        paymentInfoDomainService.validatePaymentInfo(paymentDto);

        // Then
        verify(paymentInfoRepository).existsByImpUid(any(String.class));
        verify(paymentInfoRepository).existsByMerchantUid(any(String.class));
    }

    @DisplayName("validatePaymentInfo - 결제 정보의 쿠키 가격과 실제 서버에 존재하는 쿠키 가격이 다를 때, - InvalidRequestException")
    @Test
    void validatePaymentInfo_InvalidRequestException() {
        // When, Then
        assertThatThrownBy(() -> paymentInfoDomainService.validatePaymentInfo(invalidAmountPaymentDto))
            .isInstanceOf(InvalidRequestException.class)
            .hasMessage(ErrorStatus.PAYMENT_AMOUNT_INVALID.getMessage());
    }

    @DisplayName("validatePaymentInfo - 결제 고유번호가 중복 됐을 때, - DuplicatedException (ImpUid)")
    @Test
    void validatePaymentInfo_DuplicatedException_ImpUid() {
        // Given
        given(paymentInfoRepository.existsByImpUid(any(String.class))).willReturn(true);

        // When, Then
        assertThatThrownBy(() -> paymentInfoDomainService.validatePaymentInfo(paymentDto))
            .isInstanceOf(DuplicatedException.class)
            .hasMessage(ErrorStatus.PAYMENT_IMP_UID_DUPLICATED.getMessage());
    }

    @DisplayName("validatePaymentInfo - 주문번호가 중복 됐을 때, - DuplicatedException (MerchantUid)")
    @Test
    void validatePaymentInfo_DuplicatedException_MerchantUid() {
        // Given
        given(paymentInfoRepository.existsByImpUid(any(String.class))).willReturn(false);
        given(paymentInfoRepository.existsByMerchantUid(any(String.class))).willReturn(true);

        // When, Then
        assertThatThrownBy(() -> paymentInfoDomainService.validatePaymentInfo(paymentDto))
            .isInstanceOf(DuplicatedException.class)
            .hasMessage(ErrorStatus.PAYMENT_MERCHANT_UID_DUPLICATED.getMessage());
    }

    @DisplayName("getPaymentsInfo - 해당 이메일에 대한 회원이 존재하지 않을 때, - NotFoundException")
    @Test
    void getPaymentsInfo_NotFoundException() {
        // Given
        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

        // When, Then
        assertThatThrownBy(() -> paymentInfoDomainService.getPaymentsInfo(null, memberDto))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(ErrorStatus.MEMBER_EMAIL_NOT_FOUND.getMessage());
    }

    @DisplayName("getPaymentsInfo - 조회 결과가 없을 때, - Empty List")
    @Test
    void getPaymentsInfo_EmptyList() {
        // Given
        List<PaymentInfo> paymentInfos = new ArrayList<>();
        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.of(member));
        given(paymentInfoSearchRepository.searchByMerchantsUidAndEmail(anyList(), any(String.class)))
            .willReturn(paymentInfos);

        // When
        List<PaymentInfoRes> actual = paymentInfoDomainService.getPaymentsInfo(Collections.emptyList(), memberDto);

        // Then
        assertThat(actual).isEmpty();
    }

    @DisplayName("getPaymentsInfo - 조회 결과가 2개 일때, - PaymentInfoRes List")
    @Test
    void getPaymentsInfo_PaymentInfoResList() {
        // Given
        List<PaymentInfo> paymentInfos = new ArrayList<>();
        paymentInfos.add(CreatorFactory.createPaymentInfo("imp123", "mer123", member));
        paymentInfos.add(CreatorFactory.createPaymentInfo("imp456", "mer789", member));
        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.of(member));
        given(paymentInfoSearchRepository.searchByMerchantsUidAndEmail(anyList(), any(String.class)))
            .willReturn(paymentInfos);

        // When
        List<PaymentInfoRes> actual = paymentInfoDomainService.getPaymentsInfo(Collections.emptyList(), memberDto);

        // Then
        assertThat(actual).hasSize(2);
    }
}
