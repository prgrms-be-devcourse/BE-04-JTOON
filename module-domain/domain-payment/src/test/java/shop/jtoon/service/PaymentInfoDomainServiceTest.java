package shop.jtoon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.jtoon.entity.CookieItem;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.type.ErrorStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PaymentInfoDomainServiceTest {

    @InjectMocks
    private PaymentInfoDomainService paymentInfoDomainService;

    private String itemName;
    private BigDecimal amount;

    @BeforeEach
    void beforeEach() {
        itemName = CookieItem.COOKIE_ONE.getItemName();
        amount = CookieItem.COOKIE_ONE.getAmount();
    }

    @DisplayName("validatePaymentInfo - 결제 정보의 쿠키 가격과 실제 서버에 존재하는 쿠키 가격이 같을 때, - Void")
    @Test
    void validatePaymentInfo_Void() {
        // When, Then
        assertThatNoException()
                .isThrownBy(() -> paymentInfoDomainService.validatePaymentInfo(itemName, amount));
    }

    @DisplayName("validatePaymentInfo - 결제 정보의 쿠키 가격과 실제 서버에서 알고 있는 쿠키 가격이 다를 때, - InvalidRequestException")
    @Test
    void validatePaymentInfo_InvalidRequestException() {
        // When, Then
        assertThatThrownBy(() -> paymentInfoDomainService.validatePaymentInfo(itemName, BigDecimal.ONE))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(ErrorStatus.PAYMENT_AMOUNT_INVALID.getMessage());
    }
}
