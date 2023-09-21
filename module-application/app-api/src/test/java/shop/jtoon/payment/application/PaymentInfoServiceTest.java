package shop.jtoon.payment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PaymentInfo;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.payment.factory.CreatorFactory;
import shop.jtoon.payment.request.PaymentReq;
import shop.jtoon.payment.response.PaymentRes;
import shop.jtoon.repository.PaymentInfoRepository;
import shop.jtoon.repository.PaymentInfoSearchRepository;
import shop.jtoon.service.PaymentInfoDomainService;
import shop.jtoon.type.ErrorStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentInfoServiceTest {

    @InjectMocks
    private PaymentInfoService paymentInfoService;

    @Mock
    private PaymentInfoDomainService paymentInfoDomainService;

    @Mock
    private PaymentInfoRepository paymentInfoRepository;

    @Mock
    private PaymentInfoSearchRepository paymentInfoSearchRepository;

    private Member member;
    private PaymentReq paymentReq;

    @BeforeEach
    void beforeEach() {
        member = CreatorFactory.createMember("example123@naver.com");
        paymentReq = CreatorFactory.createPaymentReq("imp123", "mer123", member.getEmail());
    }

    @DisplayName("createPaymentInfo - 한 회원의 결제 정보가 성공적으로 저장될 때, - Void")
    @Test
    void createPaymentInfo_Void() {
        // When
        paymentInfoService.createPaymentInfo(paymentReq, member);

        // Then
        verify(paymentInfoRepository).save(any(PaymentInfo.class));
    }

    @DisplayName("getPaymentsInfo - 조회 결과가 없을 때, - Empty List")
    @Test
    void getPaymentsInfo_EmptyList() {
        // Given
        List<PaymentInfo> paymentInfos = new ArrayList<>();
        given(paymentInfoSearchRepository.searchByMerchantsUidAndEmail(anyList(), any(String.class)))
                .willReturn(paymentInfos);

        // When
        List<PaymentRes> actual = paymentInfoService.getPaymentsInfo(Collections.emptyList(), member);

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
        given(paymentInfoSearchRepository.searchByMerchantsUidAndEmail(anyList(), any(String.class)))
                .willReturn(paymentInfos);

        // When
        List<PaymentRes> actual = paymentInfoService.getPaymentsInfo(Collections.emptyList(), member);

        // Then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("validatePaymentInfo - 결제 검증에 대한 서비스를 잘 호출하는 지 검증 - Void")
    @Test
    void validatePaymentInfo_Void() {
        // When
        paymentInfoService.validatePaymentInfo(paymentReq);

        // Then
        assertThatNoException()
                .isThrownBy(() -> paymentInfoDomainService.validatePaymentInfo(any(String.class), any(BigDecimal.class)));
    }

    @DisplayName("validatePaymentInfo - 결제 고유번호가 중복 됐을 때, - DuplicatedException (ImpUid)")
    @Test
    void validatePaymentInfo_DuplicatedException_ImpUid() {
        // Given
        given(paymentInfoRepository.existsByImpUid(any(String.class))).willReturn(true);

        // When, Then
        assertThatThrownBy(() -> paymentInfoService.validatePaymentInfo(paymentReq))
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
        assertThatThrownBy(() -> paymentInfoService.validatePaymentInfo(paymentReq))
                .isInstanceOf(DuplicatedException.class)
                .hasMessage(ErrorStatus.PAYMENT_MERCHANT_UID_DUPLICATED.getMessage());
    }
}
