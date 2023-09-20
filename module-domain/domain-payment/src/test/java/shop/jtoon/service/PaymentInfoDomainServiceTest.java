package shop.jtoon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.jtoon.entity.CookieItem;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.repository.PaymentInfoRepository;
import shop.jtoon.type.ErrorStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PaymentInfoDomainServiceTest {

    @InjectMocks
    private PaymentInfoDomainService paymentInfoDomainService;

    @Mock
    private PaymentInfoRepository paymentInfoRepository;

    private String impUid;
    private String merchantUid;
    private String itemName;
    private BigDecimal amount;

    @BeforeEach
    void beforeEach() {
        impUid = "impUid";
        merchantUid = "merchantUid";
        itemName = CookieItem.COOKIE_ONE.getItemName();
        amount = CookieItem.COOKIE_ONE.getAmount();
    }

    @DisplayName("validatePaymentInfo - 결제 정보에 대한 검증이 성공적으로 끝났을 때, - Void")
    @Test
    void validatePaymentInfo_Void() {
        // Given
        given(paymentInfoRepository.existsByImpUid(any(String.class))).willReturn(false);
        given(paymentInfoRepository.existsByMerchantUid(any(String.class))).willReturn(false);

        // When, Then
        assertThatNoException()
                .isThrownBy(() -> paymentInfoDomainService.validatePaymentInfo(impUid, merchantUid, itemName, amount));
    }

    @DisplayName("validatePaymentInfo - 결제 정보의 쿠키 가격과 실제 서버에 존재하는 쿠키 가격이 다를 때, - InvalidRequestException")
    @Test
    void validatePaymentInfo_InvalidRequestException() {
        // When, Then
        assertThatThrownBy(() -> paymentInfoDomainService.validatePaymentInfo(impUid, merchantUid, itemName, BigDecimal.ONE))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(ErrorStatus.PAYMENT_AMOUNT_INVALID.getMessage());
    }

    @DisplayName("validatePaymentInfo - 결제 고유번호가 중복 됐을 때, - DuplicatedException (ImpUid)")
    @Test
    void validatePaymentInfo_DuplicatedException_ImpUid() {
        // Given
        given(paymentInfoRepository.existsByImpUid(any(String.class))).willReturn(true);

        // When, Then
        assertThatThrownBy(() -> paymentInfoDomainService.validatePaymentInfo(impUid, merchantUid, itemName, amount))
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
        assertThatThrownBy(() -> paymentInfoDomainService.validatePaymentInfo(impUid, merchantUid, itemName, amount))
                .isInstanceOf(DuplicatedException.class)
                .hasMessage(ErrorStatus.PAYMENT_MERCHANT_UID_DUPLICATED.getMessage());
    }
}
