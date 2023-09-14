package shop.jtoon.service;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.type.ErrorStatus;

@Service
@Transactional(readOnly = true)
public class IamportService {

	private final IamportClient iamportClient;

	public IamportService(
		@Value("${pg.kg-inicis.rest-api-key}") String restApiKey,
		@Value("${pg.kg-inicis.rest-api-secret}") String restSecretKey
	) {
		this.iamportClient = new IamportClient(restApiKey, restSecretKey);
	}

	public void validateIamport(String impUid, BigDecimal amount) throws IamportResponseException, IOException {
		IamportResponse<Payment> irsp = iamportClient.paymentByImpUid(impUid);
		BigDecimal realAmount = irsp.getResponse().getAmount();
		validateAmount(realAmount, amount);
	}

	private void validateAmount(BigDecimal realAmount, BigDecimal amount) {
		if (!realAmount.equals(amount)) {
			throw new InvalidRequestException(ErrorStatus.PAYMENT_AMOUNT_INVALID);
		}
	}
}
