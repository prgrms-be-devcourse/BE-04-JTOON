package shop.jtoon.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import java.io.IOException;
import java.math.BigDecimal;
import shop.jtoon.exception.IamportException;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.type.ErrorStatus;

@ExtendWith(MockitoExtension.class)
class IamportServiceTest {

	@InjectMocks
	private IamportService iamportService;

	@Mock
	private IamportClient iamportClient;

	private IamportResponse<Payment> irsp;
	private Payment payment;
	private String impUid;
	private BigDecimal amount;

	@BeforeEach
	public void beforeEach() {
		try {
			impUid = "imp123";
			amount = BigDecimal.valueOf(1000);
			irsp = mock(IamportResponse.class);
			payment = mock(Payment.class);
			given(irsp.getResponse()).willReturn(payment);
			given(iamportClient.paymentByImpUid(any(String.class))).willReturn(irsp);
		} catch (IamportResponseException | IOException e) {
			throw new IamportException(e.getMessage());
		}
	}

	@DisplayName("validateIamport - 실제 결제된 금액과 프론트에서 보내준 금액이 다를 때, - InvalidRequestException")
	@Test
	void validateIamport_InvalidRequestException() {
		// Given
		given(payment.getAmount()).willReturn(amount);

		// When, Then
		assertThatThrownBy(() -> iamportService.validateIamport(impUid, BigDecimal.ONE))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessage(ErrorStatus.PAYMENT_AMOUNT_INVALID.getMessage());
	}

	@DisplayName("validateIamport - 실제 결제된 금액과 프론트에서 보내준 금액이 같을 때, - Void")
	@Test
	void validateIamport_Void() {
		// Given
		given(payment.getAmount()).willReturn(amount);

		// When, Then
		assertThatNoException().isThrownBy(() -> iamportService.validateIamport(impUid, amount));
	}

	@DisplayName("cancelIamport - 결제 취소가 성공적으로 됐을 때, - Void")
	@Test
	void cancelIamport_Void() throws IamportResponseException, IOException {
		// Given
		given(payment.getImpUid()).willReturn(impUid);
		given(iamportClient.cancelPaymentByImpUid(any(CancelData.class))).willReturn(irsp);

		// When, Then
		assertThatNoException().isThrownBy(() -> iamportService.cancelIamport(
			impUid,
			"reason",
			amount,
			"example123@naver.com")
		);
	}
}
