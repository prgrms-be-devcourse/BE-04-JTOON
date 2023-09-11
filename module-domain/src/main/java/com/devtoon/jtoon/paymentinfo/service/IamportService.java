package com.devtoon.jtoon.paymentinfo.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.paymentinfo.entity.CookieItem;
import com.devtoon.jtoon.paymentinfo.request.CancelReq;
import com.devtoon.jtoon.paymentinfo.request.PaymentReq;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

@Service
@Transactional(readOnly = true)
public class IamportService {

	private final IamportClient iamportClient;
	private final PaymentInfoService paymentInfoService;

	public IamportService(
		@Value("${pg.kg-inicis.rest-api-key}") String restApiKey,
		@Value("${pg.kg-inicis.rest-api-secret}") String restSecretKey,
		PaymentInfoService paymentInfoService
	) {
		this.iamportClient = new IamportClient(restApiKey, restSecretKey);
		this.paymentInfoService = paymentInfoService;
	}

	public IamportResponse<Payment> cancelPayment(CancelReq cancelReq)
		throws IamportResponseException, IOException {
		IamportResponse<Payment> irsp = iamportClient.paymentByImpUid(cancelReq.impUid());
		paymentInfoService.validateAmount(irsp, cancelReq.checksum());
		CancelData cancelData = cancelReq.toCancelData(irsp);

		return iamportClient.cancelPaymentByImpUid(cancelData);
	}

	public void validateIamport(PaymentReq paymentReq) throws IamportResponseException, IOException {
		IamportResponse<Payment> irsp = iamportClient.paymentByImpUid(paymentReq.impUid());
		CookieItem cookieItem = CookieItem.from(paymentReq.cookieItem());
		paymentInfoService.validateAmount(irsp, cookieItem.getAmount());
		paymentInfoService.validateAmount(irsp, paymentReq.amount());
		paymentInfoService.validateImpUid(paymentReq);
		paymentInfoService.validateMerchantUid(paymentReq);
	}
}
