package com.devtoon.jtoon.payment.service;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.payment.entity.CookieItem;
import com.devtoon.jtoon.payment.request.CancelReq;
import com.devtoon.jtoon.payment.request.PaymentReq;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

@Service
@Transactional(readOnly = true)
public class IamportService {

	private final IamportClient iamportClient;
	private final PaymentInfoDomainService paymentInfoDomainService;

	public IamportService(
		@Value("${pg.kg-inicis.rest-api-key}") String restApiKey,
		@Value("${pg.kg-inicis.rest-api-secret}") String restSecretKey,
		PaymentInfoDomainService paymentInfoDomainService
	) {
		this.iamportClient = new IamportClient(restApiKey, restSecretKey);
		this.paymentInfoDomainService = paymentInfoDomainService;
	}

	public IamportResponse<Payment> cancelPayment(CancelReq cancelReq)
		throws IamportResponseException, IOException {
		IamportResponse<Payment> irsp = iamportClient.paymentByImpUid(cancelReq.impUid());
		BigDecimal realAmount = irsp.getResponse().getAmount();
		paymentInfoDomainService.validateAmount(realAmount, cancelReq.checksum());
		CancelData cancelData = cancelReq.toCancelData(irsp);

		return iamportClient.cancelPaymentByImpUid(cancelData);
	}

	public void validateIamport(PaymentReq paymentReq) throws IamportResponseException, IOException {
		IamportResponse<Payment> irsp = iamportClient.paymentByImpUid(paymentReq.impUid());
		BigDecimal realAmount = irsp.getResponse().getAmount();
		CookieItem cookieItem = CookieItem.from(paymentReq.cookieItem());
		paymentInfoDomainService.validateAmount(realAmount, cookieItem.getAmount());
		paymentInfoDomainService.validateAmount(realAmount, paymentReq.amount());
		paymentInfoDomainService.validateImpUid(paymentReq);
		paymentInfoDomainService.validateMerchantUid(paymentReq);
	}
}
