package shop.jtoon.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.jtoon.exception.IamportException;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.type.ErrorStatus;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class IamportServiceTest {

    @InjectMocks
    private IamportService iamportService;

    @Mock
    private IamportClient iamportClient;

    private IamportResponse<Payment> irsp;
    private Payment payment;

    @BeforeEach
    public void beforeEach() {
        try {
            irsp = mock(IamportResponse.class);
            payment = mock(Payment.class);
            given(payment.getAmount()).willReturn(BigDecimal.valueOf(1000));
            given(irsp.getResponse()).willReturn(payment);
            given(iamportClient.paymentByImpUid(any(String.class))).willReturn(irsp);
        } catch (IamportResponseException | IOException e) {
            throw new IamportException(e.getMessage());
        }
    }

    @DisplayName("validateIamport - 실제 결제된 금액과 프론트에서 보내준 금액이 다를 때, - InvalidRequestException")
    @Test
    void validateIamport_InvalidRequestException() {
        // When, Then
        assertThatThrownBy(() -> iamportService.validateIamport("imp123", BigDecimal.valueOf(1)))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(ErrorStatus.PAYMENT_AMOUNT_INVALID.getMessage());
    }

    @DisplayName("validateIamport - 실제 결제된 금액과 프론트에서 보내준 금액이 같을 때, - Void")
    @Test
    void validateIamport_Void() {
        // When, Then
        assertThatNoException()
                .isThrownBy(() -> iamportService.validateIamport("imp123", BigDecimal.valueOf(1000)));
    }
}
