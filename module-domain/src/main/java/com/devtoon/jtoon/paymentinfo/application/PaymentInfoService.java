package com.devtoon.jtoon.paymentinfo.application;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.paymentinfo.entity.CookieItem;
import com.devtoon.jtoon.paymentinfo.entity.PaymentInfo;
import com.devtoon.jtoon.paymentinfo.repository.PaymentInfoRepository;
import com.devtoon.jtoon.paymentinfo.request.CancelReq;
import com.devtoon.jtoon.paymentinfo.request.PaymentReq;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

@Service
@Transactional(readOnly = true)
public class PaymentInfoService {

	private final IamportClient iamportClient;
	private final PaymentInfoRepository paymentInfoRepository;

	public PaymentInfoService(
		@Value("${pg.kg-inicis.rest-api-key}") String restApiKey,
		@Value("${pg.kg-inicis.rest-api-secret}") String restSecretKey,
		PaymentInfoRepository paymentInfoRepository
	) {
		this.iamportClient = new IamportClient(restApiKey, restSecretKey);
		this.paymentInfoRepository = paymentInfoRepository;
	}

	public IamportResponse<Payment> paymentLookUp(String impUid) throws IamportResponseException, IOException {
		return iamportClient.paymentByImpUid(impUid);
	}

	public IamportResponse<Payment> validateIamport(PaymentReq paymentReq, IamportResponse<Payment> iamportResponse) {
		CookieItem cookieItem = CookieItem.from(paymentReq.cookieItem());
		validateAmount(iamportResponse, cookieItem.getAmount());
		validateAmount(iamportResponse, paymentReq.amount());
		validateImpUid(paymentReq);
		validateMerchantUid(paymentReq);

		return iamportResponse;
	}

	@Transactional
	public BigDecimal createPaymentInfo(PaymentReq paymentReq) {
		Member member = null;
		PaymentInfo paymentInfo = paymentReq.toEntity(member);
		PaymentInfo save = paymentInfoRepository.save(paymentInfo);

		return save.getAmount();
	}

	public IamportResponse<Payment> cancelPayment(CancelReq cancelReq, IamportResponse<Payment> iamportResponse)
		throws IamportResponseException, IOException {
		validateAmount(iamportResponse, cancelReq.checksum());
		CancelData cancelData = cancelReq.toCancelData(iamportResponse);

		return iamportClient.cancelPaymentByImpUid(cancelData);
	}

	public void validateAmount(IamportResponse<Payment> iamportResponse, BigDecimal amount) {
		BigDecimal realAmount = iamportResponse.getResponse().getAmount();

		if (!realAmount.equals(amount)) {
			throw new RuntimeException("verify iamport exception");
		}
	}

	private void validateMerchantUid(PaymentReq paymentReq) {
		if (paymentInfoRepository.existsByMerchantUid(paymentReq.merchantUid())) {
			throw new RuntimeException("merchantUid duplicate");
		}
	}

	private void validateImpUid(PaymentReq paymentReq) {
		if (paymentInfoRepository.existsByImpUid(paymentReq.impUid())) {
			throw new RuntimeException("impUid duplicate");
		}
	}
}
